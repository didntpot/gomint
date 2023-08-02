package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;

public class TagItemDescriptor implements ItemDescriptor {

    private String tag;

    @Override
    public int getTypeId() {
        return ItemDescriptorType.TAG;
    }

    public TagItemDescriptor(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeString(this.tag);
    }

    public static TagItemDescriptor read(PacketBuffer buffer) {
        String tag = buffer.readString();
        return new TagItemDescriptor(tag);
    }
}
