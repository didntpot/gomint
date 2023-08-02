package io.gomint.server.network.packet.types;

import io.gomint.jraknet.PacketBuffer;

public class SpawnSettings {
    public static final short BIOME_TYPE_DEFAULT = 0;
    public static final short BIOME_TYPE_USER_DEFINED = 1;

    private short biomeType;
    private String biomeName;
    private int dimension;

    public SpawnSettings() {
        this.biomeType = BIOME_TYPE_DEFAULT;
        this.biomeName = "";
        this.dimension = 0;
    }

    public static SpawnSettings read(PacketBuffer buffer) {
        SpawnSettings result = new SpawnSettings();
        result.biomeType = buffer.readLShort();
        result.biomeName = buffer.readString();
        result.dimension = buffer.readSignedVarInt();
        return result;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeLShort(this.biomeType);
        buffer.writeString(this.biomeName);
        buffer.writeSignedVarInt(this.dimension);
    }

    public short getBiomeType() {
        return biomeType;
    }

    public String getBiomeName() {
        return biomeName;
    }

    public int getDimension() {
        return dimension;
    }
}
