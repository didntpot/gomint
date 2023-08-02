package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;

public class StringIdMetaItemDescriptor implements ItemDescriptor {

    private String id;
    private int meta;

    @Override
    public int getTypeId() {
        return ItemDescriptorType.STRING_ID_META;
    }

    public StringIdMetaItemDescriptor(
        String id,
        int meta
    ) {
        this.id = id;
        this.meta = meta;
    }

    public String getId() {
        return id;
    }

    public int getMeta() {
        return meta;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(this.id);
        buffer.writeLShort((short) this.meta);
    }

    public static StringIdMetaItemDescriptor read(PacketBuffer buffer) {
        String id = buffer.readString();
        int meta = buffer.readLShort();
        return new StringIdMetaItemDescriptor(id, meta);
    }
}
