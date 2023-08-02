package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;

public class MolangItemDescriptor implements ItemDescriptor {

    private String molangExpression;
    private byte molangVersion;

    @Override
    public int getTypeId() {
        return ItemDescriptorType.MOLANG;
    }

    public MolangItemDescriptor(String molangExpression, byte molangVersion) {
        this.molangExpression = molangExpression;
        this.molangVersion = molangVersion;
    }

    public String getMolangExpression() {
        return molangExpression;
    }

    public byte getMolangVersion() {
        return molangVersion;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(this.molangExpression);
        buffer.writeByte(this.molangVersion);
    }

    public static MolangItemDescriptor read(PacketBuffer buffer) {
        return new MolangItemDescriptor(buffer.readString(), buffer.readByte());
    }
}
