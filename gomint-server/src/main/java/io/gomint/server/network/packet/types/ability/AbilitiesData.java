package io.gomint.server.network.packet.types.ability;

import io.gomint.jraknet.PacketBuffer;

public class AbilitiesData {
    private byte commandPermission;
    private byte playerPermission;
    private long targetEntityUniqueId;
    private AbilitiesLayer[] layers;

    public AbilitiesData(byte commandPermission, byte playerPermission, int targetEntityUniqueId, AbilitiesLayer[] layers) {
        this.commandPermission = commandPermission;
        this.playerPermission = playerPermission;
        this.targetEntityUniqueId = targetEntityUniqueId;
        this.layers = layers;
    }

    public static AbilitiesData decode(PacketBuffer buffer) {
        long targetEntityRuntimeId = buffer.readLLong();
        byte playerPermission = buffer.readByte();
        byte commandPermission = buffer.readByte();
        byte abilityLayerCount = buffer.readByte();
        AbilitiesLayer[] abilitiesLayers = new AbilitiesLayer[abilityLayerCount];
        for (int i = 0; i < abilityLayerCount; i++) {
            abilitiesLayers[i] = AbilitiesLayer.decode(buffer);
        }
        return new AbilitiesData(commandPermission, playerPermission, (int) targetEntityRuntimeId, abilitiesLayers);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeLLong(this.targetEntityUniqueId);
        buffer.writeByte(this.playerPermission);
        buffer.writeByte(this.commandPermission);
        buffer.writeByte((byte) this.layers.length);
        for (AbilitiesLayer layer : this.layers) {
            layer.encode(buffer);
        }
    }

    public AbilitiesLayer[] getLayers() {
        return layers;
    }

    public byte getCommandPermission() {
        return commandPermission;
    }

    public byte getPlayerPermission() {
        return playerPermission;
    }

    public long getTargetEntityUniqueId() {
        return targetEntityUniqueId;
    }
}
