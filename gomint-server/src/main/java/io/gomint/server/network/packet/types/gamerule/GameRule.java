package io.gomint.server.network.packet.types.gamerule;

import io.gomint.jraknet.PacketBuffer;

public abstract class GameRule {

    private boolean isPlayerModifiable;

    public GameRule(boolean isPlayerModifiable) {
        this.isPlayerModifiable = isPlayerModifiable;
    }

    abstract public int getTypeId();

    abstract public void encode(PacketBuffer buffer);

    public boolean isPlayerModifiable() {
        return this.isPlayerModifiable;
    }
}
