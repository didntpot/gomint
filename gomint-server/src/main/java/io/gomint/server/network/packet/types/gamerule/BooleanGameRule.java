package io.gomint.server.network.packet.types.gamerule;

import io.gomint.jraknet.PacketBuffer;

public class BooleanGameRule extends GameRule {

    private boolean value;

    public BooleanGameRule(boolean isPlayerModifiable, boolean value) {
        super(isPlayerModifiable);
        this.value = value;
    }

    @Override
    public int getTypeId() {
        return GameRuleType.BOOLEAN;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(this.value);
    }

    public static BooleanGameRule decode(PacketBuffer buffer, boolean isPlayerModifiable) {
        return new BooleanGameRule(isPlayerModifiable, buffer.readBoolean());
    }

    public boolean getValue() {
        return value;
    }
}
