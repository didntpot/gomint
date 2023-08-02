package io.gomint.server.network.packet.types.recipe;

import javax.annotation.Nullable;

public class RecipeIngredient {

    private @Nullable ItemDescriptor itemDescriptor;
    private int count;

    public RecipeIngredient(
        @Nullable ItemDescriptor itemDescriptor,
        int count
    ) {
        this.itemDescriptor = itemDescriptor;
        this.count = count;
    }

    public @Nullable ItemDescriptor getItemDescriptor() {
        return itemDescriptor;
    }

    public int getCount() {
        return count;
    }
}
