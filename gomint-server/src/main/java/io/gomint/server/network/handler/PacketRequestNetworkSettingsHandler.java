package io.gomint.server.network.handler;

import io.gomint.server.network.PlayerConnection;
import io.gomint.server.network.PlayerConnectionState;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.PacketNetworkSettings;
import io.gomint.server.network.packet.PacketPlayState;
import io.gomint.server.network.packet.PacketRequestNetworkSettings;
import io.gomint.server.network.packet.types.CompressionAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacketRequestNetworkSettingsHandler implements PacketHandler<PacketRequestNetworkSettings> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketRequestNetworkSettingsHandler.class);

    @Override
    public void handle(PacketRequestNetworkSettings packet, long currentTimeMillis, PlayerConnection connection) throws Exception {
        if (packet.getProtocolVersion() != Protocol.MINECRAFT_PE_PROTOCOL_VERSION) {
            connection.sendPlayState(packet.getProtocolVersion() > Protocol.MINECRAFT_PE_PROTOCOL_VERSION ? PacketPlayState.PlayState.LOGIN_FAILED_SERVER : PacketPlayState.PlayState.LOGIN_FAILED_CLIENT);
            connection.disconnect("Outdated client");
            return;
        }
        PacketNetworkSettings response = new PacketNetworkSettings();
        response.setCompressionAlgorithm(PacketNetworkSettings.COMPRESS_EVERYTHING);
        response.setCompressionAlgorithm(CompressionAlgorithm.ZLIB);
        response.setEnableClientThrottling(false);
        response.setClientThrottleThreshold((byte) 0);
        response.setClientThrottleScalar(0f);
        connection.send(response);
        connection.state(PlayerConnectionState.HANDSHAKE);
        LOGGER.info("Sending network settings");
    }
}
