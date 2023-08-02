package io.gomint.server.network.packet.types.gamerule;

import io.gomint.jraknet.PacketBuffer;

public class FloatGameRule extends GameRule {

    private float value;

    public FloatGameRule(boolean isPlayerModifiable, float value) {
        super(isPlayerModifiable);
        this.value = value;
    }

    @Override
    public int getTypeId() {
        return GameRuleType.FLOAT;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeLFloat(this.value);
    }

    public static FloatGameRule decode(PacketBuffer buffer, boolean isPlayerModifiable) {
        return new FloatGameRule(isPlayerModifiable, buffer.readLFloat());
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
