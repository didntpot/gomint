package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;

public interface ItemDescriptor {

    int getTypeId();

    void write(PacketBuffer buffer);
}
