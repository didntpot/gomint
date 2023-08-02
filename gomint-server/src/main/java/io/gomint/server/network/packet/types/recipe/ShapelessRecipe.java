package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.inventory.item.ItemStack;
import io.gomint.server.network.packet.Packet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShapelessRecipe extends RecipeWithTypeId {

    private String recipeId;
    private List<RecipeIngredient> inputs;
    private List<ItemStack<?>> outputs;
    private UUID uuid;
    private String blockName;
    private int priority;
    private int recipeNetId;

    public ShapelessRecipe(
        int typeId,
        String recipeId,
        List<RecipeIngredient> inputs,
        List<ItemStack<?>> outputs,
        UUID uuid,
        String blockName,
        int priority,
        int recipeNetId
    ) {
        super(typeId);
        this.recipeId = recipeId;
        this.inputs = inputs;
        this.outputs = outputs;
        this.uuid = uuid;
        this.blockName = blockName;
        this.priority = priority;
        this.recipeNetId = recipeNetId;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public List<RecipeIngredient> getInputs() {
        return this.inputs;
    }

    public List<ItemStack<?>> getOutputs() {
        return this.outputs;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getBlockName() {
        return this.blockName;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getRecipeNetId() {
        return this.recipeNetId;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeString(this.recipeId);
        buffer.writeSignedVarInt(this.inputs.size());
        for (RecipeIngredient input : this.inputs) {
            Packet.writeRecipeIngredient(input, buffer);
        }
        buffer.writeSignedVarInt(this.outputs.size());
        for (ItemStack<?> output : this.outputs) {
            Packet.writeItemStack(output, buffer);
        }
        buffer.writeUUID(this.uuid);
        buffer.writeString(this.blockName);
        buffer.writeSignedVarInt(this.priority);
        buffer.writeSignedVarInt(this.recipeNetId);
    }

    public static ShapelessRecipe decode(int recipeType, PacketBuffer buffer) {
        String recipeId = buffer.readString();
        List<RecipeIngredient> inputs = new ArrayList<>();
        int inputCount = buffer.readSignedVarInt();
        for (int i = 0; i < inputCount; i++) {
            inputs.add(Packet.getRecipeIngredient(buffer));
        }
        List<ItemStack<?>> outputs = new ArrayList<>();
        int outputCount = buffer.readSignedVarInt();
        for (int i = 0; i < outputCount; i++) {
            outputs.add((ItemStack<?>) Packet.readItemStack(buffer));
        }
        UUID uuid = buffer.readUUID();
        String blockName = buffer.readString();
        int priority = buffer.readSignedVarInt();
        int recipeNetId = buffer.readSignedVarInt();

        return new ShapelessRecipe(recipeType, recipeId, inputs, outputs, uuid, blockName, priority, recipeNetId);
    }
}
