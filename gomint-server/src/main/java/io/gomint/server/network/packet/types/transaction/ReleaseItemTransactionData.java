package io.gomint.server.network.packet.types.transaction;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.math.Vector;
import io.gomint.server.inventory.item.ItemStack;
import io.gomint.server.network.packet.Packet;
import io.gomint.server.network.packet.PacketInventoryTransaction;

public class ReleaseItemTransactionData extends TransactionData {

    public static final int ACTION_RELEASE = 0; // bow shoot
    public static final int ACTION_CONSUME = 1; // eat food, drink potion

    private int actionType;
    private int hotbarSlot;
    private ItemStack<?> itemInHand;
    private Vector headPosition; // wtf?

    @Override
    public int getTypeId() {
        return PacketInventoryTransaction.TYPE_RELEASE_ITEM;
    }

    @Override
    public void decodeData(PacketBuffer buffer) {
        this.actionType = buffer.readUnsignedVarInt();
        this.hotbarSlot = buffer.readSignedVarInt();
        this.itemInHand = (ItemStack<?>) Packet.readItemStack(buffer);
        this.headPosition = Packet.readVector(buffer);
    }

    @Override
    public void encodeData(PacketBuffer buffer) {
        buffer.writeUnsignedVarInt(this.actionType);
        buffer.writeSignedVarInt(this.hotbarSlot);
        Packet.writeItemStack(this.itemInHand, buffer);
        Packet.writeVector(this.headPosition, buffer);
    }

    public static ReleaseItemTransactionData create(int actionType, int hotbarSlot, ItemStack<?> itemInHand, Vector headPosition) {
        ReleaseItemTransactionData result = new ReleaseItemTransactionData();
        result.actionType = actionType;
        result.hotbarSlot = hotbarSlot;
        result.itemInHand = itemInHand;
        result.headPosition = headPosition;
        return result;
    }

    public ItemStack<?> getItemInHand() {
        return itemInHand;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public int getActionType() {
        return actionType;
    }

    public Vector getHeadPosition() {
        return headPosition;
    }
}
