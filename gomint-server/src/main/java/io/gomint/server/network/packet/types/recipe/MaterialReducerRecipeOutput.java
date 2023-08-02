package io.gomint.server.network.packet.types.recipe;

public class MaterialReducerRecipeOutput {

    private int itemId;
    private int count;

    public MaterialReducerRecipeOutput(
        int itemId,
        int count
    ) {
        this.itemId = itemId;
        this.count = count;
    }

    public int getItemId() {
        return itemId;
    }

    public int getCount() {
        return count;
    }
}
