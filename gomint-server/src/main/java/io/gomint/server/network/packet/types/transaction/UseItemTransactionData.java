package io.gomint.server.network.packet.types.transaction;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.math.BlockPosition;
import io.gomint.math.Vector;
import io.gomint.server.inventory.item.ItemStack;
import io.gomint.server.network.packet.Packet;
import io.gomint.server.network.packet.PacketInventoryTransaction;
import io.gomint.world.block.data.Facing;

public class UseItemTransactionData extends TransactionData {

    public static final int ACTION_CLICK_BLOCK = 0;
    public static final int ACtION_CLICK_AIR = 1;
    public static final int ACTION_BREAK_BLOCK = 2;

    private int actionType;
    private BlockPosition blockPosition;
    private Facing face;
    private int hotbarSlot;
    private ItemStack<?> itemInHand;
    private Vector playerPosition;
    private Vector clickPosition;
    private int blockRuntimeId;

    @Override
    public int getTypeId() {
        return PacketInventoryTransaction.TYPE_USE_ITEM;
    }

    @Override
    public void decodeData(PacketBuffer buffer) {
        this.actionType = buffer.readSignedVarInt();
        this.blockPosition = Packet.readBlockPosition(buffer);
        this.face = Packet.readBlockFace(buffer);
        this.hotbarSlot = buffer.readSignedVarInt();
        this.itemInHand = (ItemStack<?>) Packet.readItemStack(buffer);
        this.playerPosition = Packet.readVector(buffer);
        this.clickPosition = Packet.readVector(buffer);
        this.blockRuntimeId = buffer.readSignedVarInt();
    }

    @Override
    public void encodeData(PacketBuffer buffer) {
        buffer.writeSignedVarInt(this.actionType);
        Packet.writeBlockPosition(this.blockPosition, buffer);
        Packet.writeBlockFace(this.face, buffer);
        buffer.writeSignedVarInt(this.hotbarSlot);
        Packet.writeItemStack(this.itemInHand, buffer);
        Packet.writeVector(this.playerPosition, buffer);
        Packet.writeVector(this.clickPosition, buffer);
        buffer.writeSignedVarInt(this.blockRuntimeId);
    }

    public static UseItemTransactionData create(int actionType, BlockPosition blockPosition, Facing face, int hotbarSlot, ItemStack<?> itemInHand, Vector playerPosition, Vector clickPosition, int blockRuntimeId) {
        UseItemTransactionData result = new UseItemTransactionData();
        result.actionType = actionType;
        result.blockPosition = blockPosition;
        result.face = face;
        result.hotbarSlot = hotbarSlot;
        result.itemInHand = itemInHand;
        result.playerPosition = playerPosition;
        result.clickPosition = clickPosition;
        result.blockRuntimeId = blockRuntimeId;
        return result;
    }

    public BlockPosition getBlockPosition() {
        return blockPosition;
    }

    public Facing getFace() {
        return face;
    }

    public int getActionType() {
        return actionType;
    }

    public int getBlockRuntimeId() {
        return blockRuntimeId;
    }

    public ItemStack<?> getItemInHand() {
        return itemInHand;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    public Vector getClickPosition() {
        return clickPosition;
    }

    public Vector getPlayerPosition() {
        return playerPosition;
    }
}
