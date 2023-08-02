package io.gomint.server.network.packet.types.transaction;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.math.Vector;
import io.gomint.server.inventory.item.ItemStack;
import io.gomint.server.network.packet.Packet;
import io.gomint.server.network.packet.PacketInventoryTransaction;

public class UseItemOnEntityTransactionData extends TransactionData {

    public static final int ACTION_INTERACT = 0;
    public static final int ACTION_ATTACK = 1;
    public static final int ACTION_ITEM_INTERACT = 2;

    private long entityRuntimeId;
    private int actionType;
    private int hotbarSlot;
    private ItemStack<?> itemInHand;
    private Vector blockPosition;
    private Vector playerPosition;

    @Override
    public int getTypeId() {
        return PacketInventoryTransaction.TYPE_USE_ITEM_ON_ENTITY;
    }

    @Override
    public void decodeData(PacketBuffer buffer) {
        this.entityRuntimeId = buffer.readUnsignedVarLong();
        this.actionType = buffer.readUnsignedVarInt();
        this.hotbarSlot = buffer.readSignedVarInt();
        this.itemInHand = (ItemStack<?>) Packet.readItemStack(buffer);
        this.blockPosition = Packet.readVector(buffer);
        this.playerPosition = Packet.readVector(buffer);
    }

    @Override
    public void encodeData(PacketBuffer buffer) {
        buffer.writeUnsignedVarLong(this.entityRuntimeId);
        buffer.writeUnsignedVarInt(this.actionType);
        buffer.writeSignedVarInt(this.hotbarSlot);
        Packet.writeItemStack(this.itemInHand, buffer);
        Packet.writeVector(this.blockPosition, buffer);
        Packet.writeVector(this.playerPosition, buffer);
    }

    public static UseItemOnEntityTransactionData create(long entityRuntimeId, int actionType, int hotbarSlot, ItemStack<?> itemInHand, Vector blockPosition, Vector playerPosition) {
        UseItemOnEntityTransactionData result = new UseItemOnEntityTransactionData();
        result.entityRuntimeId = entityRuntimeId;
        result.actionType = actionType;
        result.hotbarSlot = hotbarSlot;
        result.itemInHand = itemInHand;
        result.blockPosition = blockPosition;
        result.playerPosition = playerPosition;
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

    public Vector getPlayerPosition() {
        return playerPosition;
    }

    public long getEntityRuntimeId() {
        return entityRuntimeId;
    }

    public Vector getBlockPosition() {
        return blockPosition;
    }
}
