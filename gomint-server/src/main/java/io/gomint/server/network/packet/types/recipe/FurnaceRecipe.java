package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.inventory.item.ItemStack;
import io.gomint.server.network.packet.Packet;
import io.gomint.server.network.packet.PacketCraftingRecipes;
import javax.annotation.Nullable;

public class FurnaceRecipe extends RecipeWithTypeId {

    private int inputId;
    private @Nullable int inputMeta;
    private ItemStack<?> result;
    private String blockName;

    public FurnaceRecipe(
        int typeId,
        int inputId,
        @Nullable int inputMeta,
        ItemStack<?> result,
        String blockName
    ) {
        super(typeId);
        this.inputId = inputId;
        this.inputMeta = inputMeta;
        this.result = result;
        this.blockName = blockName;
    }

    public int getInputId() {
        return this.inputId;
    }

    public @Nullable int getInputMeta() {
        return this.inputMeta;
    }

    public ItemStack<?> getResult() {
        return this.result;
    }

    public String getBlockName() {
        return this.blockName;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeSignedVarInt(this.inputId);
        if (this.getTypeId() == PacketCraftingRecipes.ENTRY_FURNACE_DATA) {
            buffer.writeSignedVarInt(this.inputMeta);
        }
        Packet.writeItemStack(this.result, buffer);
    }

    public static FurnaceRecipe decode(int typeId, PacketBuffer buffer) {
        int inputId = buffer.readSignedVarInt();
        int inputMeta = 0;
        if (typeId == PacketCraftingRecipes.ENTRY_FURNACE_DATA) {
            inputMeta = buffer.readSignedVarInt();
        }
        ItemStack<?> result = (ItemStack<?>) Packet.readItemStack(buffer);
        String blockName = "";
        if (typeId == PacketCraftingRecipes.ENTRY_FURNACE_DATA) {
            blockName = buffer.readString();
        }
        return new FurnaceRecipe(typeId, inputId, inputMeta, result, blockName);
    }
}
