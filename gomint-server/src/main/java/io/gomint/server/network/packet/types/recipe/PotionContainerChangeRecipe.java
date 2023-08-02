package io.gomint.server.network.packet.types.recipe;

public class PotionContainerChangeRecipe {

    private int inputItemId;
    private int ingredientItemId;
    private int outputItemId;

    public PotionContainerChangeRecipe(
        int inputItemId,
        int ingredientItemId,
        int outputItemId
    ) {
        this.inputItemId = inputItemId;
        this.ingredientItemId = ingredientItemId;
        this.outputItemId = outputItemId;
    }

    public int getInputItemId() {
        return inputItemId;
    }

    public int getIngredientItemId() {
        return ingredientItemId;
    }

    public int getOutputItemId() {
        return outputItemId;
    }
}
