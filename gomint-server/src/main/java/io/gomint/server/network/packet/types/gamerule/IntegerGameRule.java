package io.gomint.server.network.packet.types.gamerule;

import io.gomint.jraknet.PacketBuffer;

public class IntegerGameRule extends GameRule {

    private int value;

    public IntegerGameRule(boolean isPlayerModifiable, int value) {
        super(isPlayerModifiable);
        this.value = value;
    }

    @Override
    public int getTypeId() {
        return GameRuleType.INTEGER;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUnsignedVarInt(this.value);
    }

    public static IntegerGameRule decode(PacketBuffer buffer, boolean isPlayerModifiable) {
        return new IntegerGameRule(isPlayerModifiable, buffer.readUnsignedVarInt());
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
