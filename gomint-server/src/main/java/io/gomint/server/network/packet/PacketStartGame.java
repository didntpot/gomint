package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.math.Location;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.types.LevelSettings;
import io.gomint.server.network.packet.types.NetworkPermissions;
import io.gomint.server.network.packet.types.PlayerMovementSettings;
import io.gomint.taglib.NBTTagCompound;
import io.gomint.taglib.NBTWriter;
import java.io.IOException;
import java.nio.ByteOrder;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketStartGame extends Packet implements PacketClientbound {

    public static final short BIOME_TYPE_DEFAULT = 0;
    public static final short BIOME_TYPE_USER_DEFINED = 1;

    // Entity data
    private long entityId;
    private long runtimeEntityId;
    private int gamemode;
    private Location location;

    private float pitch;
    private float yaw;

    private NBTTagCompound playerActorProperties;

    private LevelSettings levelSettings;

    // World data
    private String levelId;
    private String worldName;
    private String templateId;
    private boolean isTrial;
    private PlayerMovementSettings playerMovementSettings;
    private long currentTick;
    private int enchantmentSeed;

    // Server stuff
    private String correlationId;
    private boolean enableNewInventorySystem = true; // TODO: use new inventory system
    private String serverSoftwareVersion;
    private String worldTemplateId;
    private boolean enableClientSideChunkGeneration;
    private boolean blockNetworkIdsAreHashes; // fnv32 hashed block ids
    private NetworkPermissions networkPermissions;

    // Lookup tables
    private PacketBuffer blockPalette;
    private int blockPaletteChecksum;

    private PacketBuffer itemPalette;


    /**
     * Create a new start game packet
     */
    public PacketStartGame() {
        super(Protocol.PACKET_START_GAME);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeSignedVarLong(this.entityId); // EntityUnique
        buffer.writeUnsignedVarLong(this.runtimeEntityId); // EntityRuntime
        buffer.writeSignedVarInt(this.gamemode); // VarInt
        buffer.writeLFloat(this.location.x()); // Vec3
        buffer.writeLFloat(this.location.y());
        buffer.writeLFloat(this.location.z());
        buffer.writeLFloat(this.location.yaw()); // Vec2
        buffer.writeLFloat(this.location.pitch());

        this.levelSettings.write(buffer);
        buffer.writeString(this.levelId);
        buffer.writeString(this.worldName);
        buffer.writeString(this.templateId);
        buffer.writeBoolean(this.isTrial);
        this.playerMovementSettings.write(buffer);
        buffer.writeLLong(this.currentTick);
        buffer.writeSignedVarInt(this.enchantmentSeed);

        buffer.writeUnsignedVarInt(this.blockPalette.getRemaining());
        buffer.writeBytes(this.blockPalette.getBuffer());

        buffer.writeUnsignedVarInt(this.itemPalette.getRemaining());
        buffer.writeBytes(this.itemPalette.getBuffer());

        buffer.writeString(this.correlationId);
        buffer.writeBoolean(this.enableNewInventorySystem);
        buffer.writeString(this.serverSoftwareVersion);
        NBTWriter writer = new NBTWriter(buffer.getBuffer(), ByteOrder.LITTLE_ENDIAN);
        writer.setUseVarint(true);
        try {
            writer.write(this.playerActorProperties);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to write NBT", e);
        }
        buffer.writeLLong(this.blockPaletteChecksum);
        buffer.writeString(this.worldTemplateId);
        buffer.writeBoolean(this.enableClientSideChunkGeneration);
        buffer.writeBoolean(this.blockNetworkIdsAreHashes);
        this.networkPermissions.encode(buffer);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        // TODO
    }

    // add all getters
    public long getEntityId() {
        return this.entityId;
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public int getGamemode() {
        return this.gamemode;
    }

    public Location getLocation() {
        return this.location;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public NBTTagCompound getPlayerActorProperties() {
        return this.playerActorProperties;
    }

    public LevelSettings getLevelSettings() {
        return this.levelSettings;
    }

    public String getLevelId() {
        return this.levelId;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public boolean isTrial() {
        return this.isTrial;
    }

    public PlayerMovementSettings getPlayerMovementSettings() {
        return this.playerMovementSettings;
    }

    public long getCurrentTick() {
        return this.currentTick;
    }

    public int getEnchantmentSeed() {
        return this.enchantmentSeed;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public boolean isEnableNewInventorySystem() {
        return this.enableNewInventorySystem;
    }

    public String getServerSoftwareVersion() {
        return this.serverSoftwareVersion;
    }

    public String getWorldTemplateId() {
        return this.worldTemplateId;
    }

    public boolean isEnableClientSideChunkGeneration() {
        return this.enableClientSideChunkGeneration;
    }

    public boolean isBlockNetworkIdsAreHashes() {
        return this.blockNetworkIdsAreHashes;
    }

    public NetworkPermissions getNetworkPermissions() {
        return this.networkPermissions;
    }

    public PacketBuffer getBlockPalette() {
        return this.blockPalette;
    }

    public int getBlockPaletteChecksum() {
        return this.blockPaletteChecksum;
    }

    public PacketBuffer getItemPalette() {
        return this.itemPalette;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPlayerActorProperties(NBTTagCompound playerActorProperties) {
        this.playerActorProperties = playerActorProperties;
    }

    public void setLevelSettings(LevelSettings levelSettings) {
        this.levelSettings = levelSettings;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public void setTrial(boolean isTrial) {
        this.isTrial = isTrial;
    }

    public void setPlayerMovementSettings(PlayerMovementSettings playerMovementSettings) {
        this.playerMovementSettings = playerMovementSettings;
    }

    public void setCurrentTick(long currentTick) {
        this.currentTick = currentTick;
    }

    public void setEnchantmentSeed(int enchantmentSeed) {
        this.enchantmentSeed = enchantmentSeed;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setEnableNewInventorySystem(boolean enableNewInventorySystem) {
        this.enableNewInventorySystem = enableNewInventorySystem;
    }

    public void setServerSoftwareVersion(String serverSoftwareVersion) {
        this.serverSoftwareVersion = serverSoftwareVersion;
    }

    public void setWorldTemplateId(String worldTemplateId) {
        this.worldTemplateId = worldTemplateId;
    }

    public void setEnableClientSideChunkGeneration(boolean enableClientSideChunkGeneration) {
        this.enableClientSideChunkGeneration = enableClientSideChunkGeneration;
    }

    public void setBlockNetworkIdsAreHashes(boolean blockNetworkIdsAreHashes) {
        this.blockNetworkIdsAreHashes = blockNetworkIdsAreHashes;
    }

    public void setNetworkPermissions(NetworkPermissions networkPermissions) {
        this.networkPermissions = networkPermissions;
    }

    public void setBlockPalette(PacketBuffer blockPalette) {
        this.blockPalette = blockPalette;
    }

    public void setBlockPaletteChecksum(int blockPaletteChecksum) {
        this.blockPaletteChecksum = blockPaletteChecksum;
    }

    public void setItemPalette(PacketBuffer itemPalette) {
        this.itemPalette = itemPalette;
    }
}
