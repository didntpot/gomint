package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;

public class ComplexAliasItemDescriptor implements ItemDescriptor {

    private String alias;

    @Override
    public int getTypeId() {
        return ItemDescriptorType.COMPLEX_ALIAS;
    }

    public ComplexAliasItemDescriptor(String alias) {
        this.alias = alias;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(this.alias);
    }

    public static ComplexAliasItemDescriptor read(PacketBuffer buffer) {
        return new ComplexAliasItemDescriptor(buffer.readString());
    }
}
