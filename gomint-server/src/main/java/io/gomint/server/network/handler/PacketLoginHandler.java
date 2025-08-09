/*
 *  Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 *  This code is licensed under the BSD license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.handler;

import io.gomint.event.player.PlayerLoginEvent;
import io.gomint.player.DeviceInfo;
import io.gomint.server.GoMintServer;
import io.gomint.server.config.ServerConfig;
import io.gomint.server.entity.EntityPlayer;
import io.gomint.server.jwt.*;
import io.gomint.server.network.*;
import io.gomint.server.network.packet.PacketEncryptionRequest;
import io.gomint.server.network.packet.PacketLogin;
import io.gomint.server.network.packet.PacketPlayState;
import io.gomint.server.network.packet.PacketResourcePacksInfo;
import io.gomint.server.player.PlayerSkin;
import io.gomint.server.scheduler.SyncScheduledTask;
import io.gomint.server.world.WorldAdapter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.gomint.player.DeviceInfo.DeviceOS.*;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketLoginHandler implements PacketHandler<PacketLogin> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketLoginHandler.class);
    private static final EncryptionRequestForger FORGER = new EncryptionRequestForger();
    private static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-z0-9_\\. ]{1,16}");

    private final EncryptionKeyFactory keyFactory;
    private final ServerConfig serverConfig;
    private final GoMintServer server;

    public PacketLoginHandler(EncryptionKeyFactory keyFactory, ServerConfig serverConfig, GoMintServer server) {
        this.keyFactory = keyFactory;
        this.serverConfig = serverConfig;
        this.server = server;
    }

    @Override
    public void handle(PacketLogin packet, long currentTimeMillis, PlayerConnection connection) {
        // We set the decompression limit to 500kb
        connection.inputProcessor().preallocSize(500 * 1024);

        // Check versions
        LOGGER.info("Trying to login with protocol version: " + packet.getProtocol());

        // Set the protocol id into the connection
        connection.protocolID(packet.getProtocol());

        // Async login sequence
        connection.server().executorService().execute(() -> {
            // More data please
            ByteBuffer byteBuffer = ByteBuffer.wrap(packet.getPayload());
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            byte[] stringBuffer = new byte[byteBuffer.getInt()];
            byteBuffer.get(stringBuffer);

            // Parse chain and validate
            String jwt = new String(stringBuffer);
            JSONObject json;
            try {
                json = parseJwtString(jwt);
            } catch (ParseException e) {
                LOGGER.warn("Error in parsing JWT for login: ", e);
                return;
            }

            Object jsonChainRaw = json.get("chain");
            if (!(jsonChainRaw instanceof JSONArray)) {
                return;
            }

            MojangChainValidator chainValidator = new MojangChainValidator(this.keyFactory);
            JSONArray jsonChain = (JSONArray) jsonChainRaw;
            for (Object jsonTokenRaw : jsonChain) {
                if (jsonTokenRaw instanceof String) {
                    try {
                        JwtToken token = JwtToken.parse((String) jsonTokenRaw);
                        chainValidator.addToken(token);
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("Invalid token in keychain for login: ", e);
                    }
                }
            }

            boolean valid = chainValidator.validate();

            // Parse skin
            byte[] skin = new byte[byteBuffer.getInt()];
            byteBuffer.get(skin);

            JwtToken skinToken = JwtToken.parse(new String(skin));
            boolean validSkin = true;

            try {
                skinToken.validateSignature(JwtAlgorithm.ES384, chainValidator.getClientPublicKey());
            } catch (JwtSignatureException e) {
                LOGGER.warn("Invalid skin in login: ", e);
                validSkin = false;
            }

            // Sync up for disconnecting etc.
            boolean finalValidSkin = validSkin;
            connection.server().syncTaskManager().addTask(new SyncScheduledTask(connection.server().syncTaskManager(), () -> {
                // Invalid skin
                if (!finalValidSkin) {
                    connection.disconnect("Skin is invalid or corrupted");
                    return;
                }

                // Check if valid user (xbox live)
                if (!valid) {
                    connection.disconnect("Only valid XBOX Logins are allowed");
                    return;
                }

                // Check for names
                String name = chainValidator.getUsername();
                if (!name.isEmpty() && name.length() <= 16) {
                    if (!NAME_PATTERN.matcher(name).matches()) {
                        connection.disconnect("disconnectionScreen.invalidName");
                        return;
                    }
                } else {
                    connection.disconnect("disconnectionScreen.invalidName");
                    return;
                }

                // Check for name / uuid collision
                for (io.gomint.entity.EntityPlayer player : this.server.onlinePlayers()) {
                    if (player.name().equals(name) ||
                        player.uuid().equals(chainValidator.getUuid())) {
                        connection.disconnect("Player already logged in on this server");
                        return;
                    }
                }

                List<JSONObject> animationList = new ArrayList<>();
                JSONArray animatedImageData = skinToken.getClaim("AnimatedImageData");
                for (Object animationObj : animatedImageData) {
                    JSONObject animation = (JSONObject) animationObj;
                    animationList.add(animation);
                }

                List<JSONObject> personaPieceList = new ArrayList<>();
                JSONArray personaPieces = skinToken.getClaim("PersonaPieces");
                for (Object personaPieceObj : personaPieces) {
                    JSONObject personaPiece = (JSONObject) personaPieceObj;
                    personaPieceList.add(personaPiece);
                }

                List<JSONObject> pieceTintColorList = new ArrayList<>();
                JSONArray pieceTintColors = skinToken.getClaim("PieceTintColors");
                for (Object pieceTintColorObj : pieceTintColors) {
                    JSONObject pieceTintColor = (JSONObject) pieceTintColorObj;
                    pieceTintColorList.add(pieceTintColor);
                }

                PlayerSkin playerSkin = new PlayerSkin(skinToken.getClaim("SkinId"),
                    new String(Base64.getDecoder().decode((String) skinToken.getClaim("SkinResourcePatch"))),
                    Math.toIntExact(skinToken.getClaim("SkinImageWidth")),
                    Math.toIntExact(skinToken.getClaim("SkinImageHeight")),
                    Base64.getDecoder().decode((String) skinToken.getClaim("SkinData")),
                    animationList,
                    Math.toIntExact(skinToken.getClaim("CapeImageWidth")),
                    Math.toIntExact(skinToken.getClaim("CapeImageHeight")),
                    Base64.getDecoder().decode((String) skinToken.getClaim("CapeData")),
                    new String(Base64.getDecoder().decode((String) skinToken.getClaim("SkinGeometryData"))),
                    new String(Base64.getDecoder().decode((String) skinToken.getClaim("SkinAnimationData"))),
                    skinToken.getClaim("PremiumSkin"),
                    skinToken.getClaim("PersonaSkin"),
                    skinToken.getClaim("CapeOnClassicSkin"),
                    skinToken.getClaim("CapeId"),
                    skinToken.getClaim("SkinColor"),
                    skinToken.getClaim("ArmSize"),
                    personaPieceList,
                    pieceTintColorList
                );

                // Create needed device info
                DeviceInfo deviceInfo = new DeviceInfo(
                    getDeviceOSFrom(Math.toIntExact(skinToken.getClaim("DeviceOS"))),
                    skinToken.getClaim("DeviceModel"),
                    skinToken.getClaim("DeviceId"),
                    getUIFrom(Math.toIntExact(skinToken.getClaim("UIProfile"))));
                connection.deviceInfo(deviceInfo);

                // Detect language
                String languageCode = skinToken.getClaim("LanguageCode");
                Locale locale;
                if (languageCode != null) {
                    locale = Locale.forLanguageTag(languageCode.replace("_", "-"));
                } else {
                    locale = Locale.US;
                }

                // Create entity:
                WorldAdapter world = this.server.defaultWorld();

                EntityPlayer player = new EntityPlayer(world, connection, chainValidator.getUsername(),
                    chainValidator.getXboxId(), chainValidator.getUuid(), locale, this.server.pluginManager());

                connection.entity(player);
                connection.entity().skin(playerSkin);
                connection.entity().nameTagVisible(true);
                connection.entity().nameTagAlwaysVisible(true);
                connection.entity().loginPerformance().setLoginPacket(currentTimeMillis);
                connection.entity().loginPerformance().setEncryptionStart(currentTimeMillis);

                // Fill in fast access maps
                connection.server().uuidMappedPlayers().put(chainValidator.getUuid(), connection.entity());

                // Post login event
                PlayerLoginEvent event = new PlayerLoginEvent(connection.entity());

                // Default deny for maximum amount of players
                if (connection.server().onlinePlayers().size() >= connection.server().serverConfig().maxPlayers()) {
                    event.cancelled(true);
                    event.kickMessage("Server is full");
                }

                this.server.pluginManager().callEvent(event);
                if (event.cancelled()) {
                    connection.disconnect(event.kickMessage());
                    return;
                }

                // FIXME: Current Minecraft uses GCM to enable encryption between server and client
                // the current implementation still uses CFB8, so we won't enable this until we get proper implementation
                // See: https://github.com/gomint/crypto/issues/2

//                if (this.keyFactory.keyPair() == null) {
                // No encryption

                PacketPlayState playState = new PacketPlayState();
                playState.setState(PacketPlayState.PlayState.LOGIN_SUCCESS);
                connection.addToSendQueue(playState);

                connection.state(PlayerConnectionState.RESOURCE_PACK);

                PacketResourcePacksInfo resourcePacksInfo = new PacketResourcePacksInfo();
                connection.addToSendQueue(resourcePacksInfo);

//                } else {
//                    // Generating EDCH secrets can take up huge amount of time
//                    connection.server().executorService().execute(() -> {
//                        connection.server().watchdog().add(2, TimeUnit.SECONDS);
//
//                        // Enable encryption
//                        EncryptionHandler encryptionHandler = new EncryptionHandler(this.keyFactory);
//                        encryptionHandler.supplyClientKey(chainValidator.getClientPublicKey());
//                        if (encryptionHandler.beginClientsideEncryption()) {
//                            // Get the needed data for the encryption start
//                            connection.state(PlayerConnectionState.ENCRPYTION_INIT);
//
//                            byte[] key = encryptionHandler.key();
//                            byte[] iv = encryptionHandler.iv();
//
//                            // We need every packet to be encrypted from now on
//                            connection.inputProcessor().enableCrypto(key, iv);
//
//                            // Forge a JWT
//                            String encryptionRequestJWT = FORGER.forge(encryptionHandler.serverPublic(), encryptionHandler.serverPrivate(), encryptionHandler.clientSalt());
//
//                            // Tell the client to enable encryption after sending that packet we also enable it
//                            PacketEncryptionRequest packetEncryptionRequest = new PacketEncryptionRequest();
//                            packetEncryptionRequest.setJwt(encryptionRequestJWT);
//                            connection.send(packetEncryptionRequest, aVoid -> {
//                                connection.outputProcessor().enableCrypto(key, iv);
//                            });
//                        }
//
//                        connection.server().watchdog().done();
//                    });
//
//                }
            }, 1, -1, TimeUnit.MILLISECONDS));
        });
    }

    private DeviceInfo.UI getUIFrom(int uiProfile) {
        return uiProfile == 0 ? DeviceInfo.UI.CLASSIC : DeviceInfo.UI.POCKET;
    }

    /**
     * Parses the specified JSON string and ensures it is a JSONObject.
     *
     * @param jwt The string to parse
     * @return The parsed JSON object on success
     * @throws ParseException Thrown if the given JSON string is invalid or does not start with a JSONObject
     */
    private JSONObject parseJwtString(String jwt) throws ParseException {
        Object jsonParsed = new JSONParser().parse(jwt);
        if (jsonParsed instanceof JSONObject) {
            return (JSONObject) jsonParsed;
        } else {
            throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
        }
    }


    private DeviceInfo.DeviceOS getDeviceOSFrom(int value) {
        switch (value) {
            case 1:
                return ANDROID;
            case 2:
                return IOS;
            case 7:
                return WINDOWS;
            default:
                LOGGER.warn("Unknown device OS ID: " + value);
                return null;
        }
    }

}
