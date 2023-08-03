package io.gomint.server.network.packet.types.entity;

import io.gomint.jraknet.PacketBuffer;

public class AttributeModifier {

    private String id;
    private String name;
    private float amount;
    private int operation;
    private int operand;
    private boolean serializable; // ???

    public AttributeModifier(String id, String name, float amount, int operation, int operand, boolean serializable) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.operand = operand;
        this.serializable = serializable;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public float getAmount() {
        return this.amount;
    }

    public int getOperation() {
        return this.operation;
    }

    public int getOperand() {
        return this.operand;
    }

    public boolean isSerializable() {
        return this.serializable;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeString(this.id);
        buffer.writeString(this.name);
        buffer.writeLFloat(this.amount);
        buffer.writeLInt(this.operation);
        buffer.writeLInt(this.operand);
        buffer.writeBoolean(this.serializable);
    }

    public static AttributeModifier read(PacketBuffer buffer) {
        String id = buffer.readString();
        String name = buffer.readString();
        float amount = buffer.readLFloat();
        int operation = buffer.readLInt();
        int operand = buffer.readLInt();
        boolean serializable = buffer.readBoolean();
        return new AttributeModifier(id, name, amount, operation, operand, serializable);
    }
}
