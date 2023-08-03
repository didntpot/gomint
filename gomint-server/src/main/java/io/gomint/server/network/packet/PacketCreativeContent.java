/*
 * Copyright (c) 2020 Gomint team
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.inventory.item.ItemStack;
import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketCreativeContent extends Packet implements PacketClientbound {

    private ItemStack<?>[] items;

    public PacketCreativeContent() {
        super(Protocol.PACKET_CREATIVE_CONTENT);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeUnsignedVarInt(0);
//        buffer.writeUnsignedVarInt(this.items.length);
//        for (ItemStack<?> item : this.items) {
////            buffer.writeUnsignedVarInt(((io.gomint.server.inventory.item.ItemStack<?>) item).stackId());
////            writeItemStack(item, buffer);
//            io.gomint.server.inventory.item.ItemStack<?> serverItem = (io.gomint.server.inventory.item.ItemStack<?>) item;
//            buffer.writeUnsignedVarInt(serverItem.stackId());
//            writeItemStack(serverItem, buffer);
//        }
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {

    }

    public ItemStack<?>[] getItems() {
        return this.items;
    }

    public void setItems(ItemStack<?>[] items) {
        this.items = items;
    }

}
