package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;

public class IntIdMetaItemDescriptor implements ItemDescriptor {

    private int id;
    private int meta;

    @Override
    public int getTypeId() {
        return ItemDescriptorType.INT_ID_META;
    }

    public IntIdMetaItemDescriptor(int id, int meta) {
        if (id == 0 && meta != 0) {
            throw new IllegalArgumentException("Meta cannot be a non-zero for air");
        }
        this.id = id;
        this.meta = meta;
    }

    public int getId() {
        return id;
    }

    public int getMeta() {
        return meta;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeLShort((short) this.id);
        if (this.id != 0) {
            buffer.writeLShort((short) this.meta);
        }
    }

    public static IntIdMetaItemDescriptor read(PacketBuffer buffer) {
        int id = buffer.readLShort();
        int meta = 0;
        if (id != 0) {
            meta = buffer.readLShort();
        }
        return new IntIdMetaItemDescriptor(id, meta);
    }
}
