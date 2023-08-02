package io.gomint.server.network.packet.types;

import io.gomint.jraknet.PacketBuffer;

public class PlayerMovementSettings {

    private int movementType;
    private int rewindHistorySize;
    private boolean serverAuthoritativeBlockBreaking;

    public PlayerMovementSettings(int movementType, int rewindHistorySize, boolean serverAuthoritativeBlockBreaking) {
        this.movementType = movementType;
        this.rewindHistorySize = rewindHistorySize;
        this.serverAuthoritativeBlockBreaking = serverAuthoritativeBlockBreaking;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeSignedVarInt(this.movementType);
        buffer.writeSignedVarInt(this.rewindHistorySize);
        buffer.writeBoolean(this.serverAuthoritativeBlockBreaking);
    }

    public static PlayerMovementSettings read(PacketBuffer buffer) {
        int movementType = buffer.readSignedVarInt();
        int rewindHistorySize = buffer.readSignedVarInt();
        boolean serverAuthoritativeBlockBreaking = buffer.readBoolean();
        return new PlayerMovementSettings(movementType, rewindHistorySize, serverAuthoritativeBlockBreaking);
    }

    public int getMovementType() {
        return movementType;
    }

    public int getRewindHistorySize() {
        return rewindHistorySize;
    }

    public boolean isServerAuthoritativeBlockBreaking() {
        return serverAuthoritativeBlockBreaking;
    }
}
