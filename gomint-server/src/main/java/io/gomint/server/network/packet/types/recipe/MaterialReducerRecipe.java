package io.gomint.server.network.packet.types.recipe;

import java.util.List;

public class MaterialReducerRecipe {
    private int inputItemId;
    private int inputItemMeta;
    private List<MaterialReducerRecipeOutput> outputs;

    public MaterialReducerRecipe(
        int inputItemId,
        int inputItemMeta,
        List<MaterialReducerRecipeOutput> outputs
    ) {
        this.inputItemId = inputItemId;
        this.inputItemMeta = inputItemMeta;
        this.outputs = outputs;
    }

    public int getInputItemId() {
        return inputItemId;
    }

    public int getInputItemMeta() {
        return inputItemMeta;
    }

    public List<MaterialReducerRecipeOutput> getOutputs() {
        return outputs;
    }
}
