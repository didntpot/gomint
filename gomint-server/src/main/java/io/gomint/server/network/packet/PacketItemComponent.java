/*
 * Copyright (c) 2020 Gomint team
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.types.ItemComponentPacketEntry;
import io.gomint.taglib.NBTWriter;
import java.nio.ByteOrder;

public class PacketItemComponent extends Packet implements PacketClientbound {

    private ItemComponentPacketEntry[] entries = new ItemComponentPacketEntry[0];

    public PacketItemComponent() {
        super(Protocol.PACKET_ITEM_COMPONENT);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) throws Exception {
        buffer.writeUnsignedVarInt(this.entries.length);
        NBTWriter writer = new NBTWriter(buffer.getBuffer(), ByteOrder.LITTLE_ENDIAN);
        for (ItemComponentPacketEntry entry : this.entries) {
            buffer.writeString(entry.getName());
            writer.setUseVarint(true);
            writer.write(entry.getComponentNbt());
        }
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {

    }

}
