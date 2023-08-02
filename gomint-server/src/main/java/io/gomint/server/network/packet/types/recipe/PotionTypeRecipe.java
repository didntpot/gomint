package io.gomint.server.network.packet.types.recipe;

public class PotionTypeRecipe {

    private int inputItemId;
    private int inputItemMeta;
    private int ingredientItemId;
    private int ingredientItemMeta;
    private int outputItemId;
    private int outputItemMeta;

    public PotionTypeRecipe(
        int inputItemId,
        int inputItemMeta,
        int ingredientItemId,
        int ingredientItemMeta,
        int outputItemId,
        int outputItemMeta
    ) {
        this.inputItemId = inputItemId;
        this.inputItemMeta = inputItemMeta;
        this.ingredientItemId = ingredientItemId;
        this.ingredientItemMeta = ingredientItemMeta;
        this.outputItemId = outputItemId;
        this.outputItemMeta = outputItemMeta;
    }

    public int getInputItemId() {
        return inputItemId;
    }

    public int getInputItemMeta() {
        return inputItemMeta;
    }

    public int getIngredientItemId() {
        return ingredientItemId;
    }

    public int getIngredientItemMeta() {
        return ingredientItemMeta;
    }

    public int getOutputItemId() {
        return outputItemId;
    }

    public int getOutputItemMeta() {
        return outputItemMeta;
    }
}
