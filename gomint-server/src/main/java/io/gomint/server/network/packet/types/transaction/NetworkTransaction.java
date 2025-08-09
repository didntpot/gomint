package io.gomint.server.network.packet.types.transaction;

import io.gomint.inventory.item.ItemStack;
import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.packet.Packet;

import javax.annotation.Nonnull;

public class NetworkTransaction {
    public static final int SOURCE_CONTAINER = 0;

    public static final int SOURCE_WORLD = 2; //drop/pickup item entity
    public static final int SOURCE_CREATIVE = 3;
    public static final int SOURCE_TODO = 99999;

    /**
     * Fake window IDs for the SOURCE_TODO type (99999)
     * <p>
     * These identifiers are used for inventory source types which are not currently implemented server-side in MCPE.
     * As a general rule of thumb, anything that doesn't have a permanent inventory is client-side. These types are
     * to allow servers to track what is going on in client-side windows.
     * <p>
     * Expect these to change in the future.
     */
    public static final int SOURCE_TYPE_CRAFTING_RESULT = -4;
    public static final int SOURCE_TYPE_CRAFTING_USE_INGREDIENT = -5;

    public static final int SOURCE_TYPE_ANVIL_RESULT = -12;
    public static final int SOURCE_TYPE_ANVIL_OUTPUT = -13;

    public static final int SOURCE_TYPE_ENCHANT_OUTPUT = -17;

    public static final int SOURCE_TYPE_TRADING_INPUT_1 = -20;
    public static final int SOURCE_TYPE_TRADING_INPUT_2 = -21;
    public static final int SOURCE_TYPE_TRADING_USE_INPUTS = -22;
    public static final int SOURCE_TYPE_TRADING_OUTPUT = -23;

    public static final int SOURCE_TYPE_BEACON = -24;

    public static final int ACTION_MAGIC_SLOT_CREATIVE_DELETE_ITEM = 0;
    public static final int ACTION_MAGIC_SLOT_CREATIVE_CREATE_ITEM = 1;

    public static final int ACTION_MAGIC_SLOT_DROP_ITEM = 0;
    public static final int ACTION_MAGIC_SLOT_PICKUP_ITEM = 1;

    private int sourceType;
    private int windowId;
    private int sourceFlags;
    private int slot;
    private ItemStack<?> oldItem;
    private ItemStack<?> newItem;

    public @Nonnull NetworkTransaction read(PacketBuffer buffer) {
        this.sourceType = buffer.readUnsignedVarInt();

        switch (this.sourceType) {
            case SOURCE_CONTAINER:
            case SOURCE_TODO:
                this.windowId = buffer.readSignedVarInt();
                break;
            case SOURCE_WORLD:
                this.sourceFlags = buffer.readUnsignedVarInt();
                break;
            case SOURCE_CREATIVE:
                break;
            default:
                // TODO
        }
        this.slot = buffer.readUnsignedVarInt();
        this.oldItem = Packet.readItemStackWithID(buffer);
        this.newItem = Packet.readItemStackWithID(buffer);

        return this;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeUnsignedVarInt(this.sourceType);

        switch (this.sourceType) {
            case SOURCE_CONTAINER:
            case SOURCE_TODO:
                buffer.writeSignedVarInt(this.windowId);
                break;
            case SOURCE_WORLD:
                buffer.writeUnsignedVarInt(this.sourceFlags);
                break;
            case SOURCE_CREATIVE:
                break;
            default:
                // TODO
        }
        buffer.writeUnsignedVarInt(this.slot);
        Packet.writeItemStackWithID(this.oldItem, buffer);
        Packet.writeItemStackWithID(this.newItem, buffer);
    }

    public int getSourceType() {
        return this.sourceType;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getSourceFlags() {
        return this.sourceFlags;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack<?> getOldItem() {
        return this.oldItem;
    }

    public ItemStack<?> getNewItem() {
        return this.newItem;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
