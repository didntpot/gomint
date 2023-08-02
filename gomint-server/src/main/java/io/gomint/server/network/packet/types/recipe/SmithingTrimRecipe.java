package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.packet.Packet;

public class SmithingTrimRecipe extends RecipeWithTypeId {

    private String recipeId;
    private RecipeIngredient template;
    private RecipeIngredient input;
    private RecipeIngredient addition;
    private String blockName;
    private int recipeNetId;

    public SmithingTrimRecipe(
        int typeId,
        String recipeId,
        RecipeIngredient template,
        RecipeIngredient input,
        RecipeIngredient addition,
        String blockName,
        int recipeNetId
    ) {
        super(typeId);
        this.recipeId = recipeId;
        this.template = template;
        this.input = input;
        this.addition = addition;
        this.blockName = blockName;
        this.recipeNetId = recipeNetId;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public RecipeIngredient getTemplate() {
        return this.template;
    }

    public RecipeIngredient getInput() {
        return input;
    }

    public RecipeIngredient getAddition() {
        return this.addition;
    }

    public String getBlockName() {
        return this.blockName;
    }

    public int getRecipeNetId() {
        return recipeNetId;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeString(this.recipeId);
        Packet.writeRecipeIngredient(this.template, buffer);
        Packet.writeRecipeIngredient(this.input, buffer);
        Packet.writeRecipeIngredient(this.addition, buffer);
        buffer.writeString(this.blockName);
        buffer.writeSignedVarInt(this.recipeNetId);
    }
}
