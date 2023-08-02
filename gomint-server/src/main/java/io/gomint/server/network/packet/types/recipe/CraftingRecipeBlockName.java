package io.gomint.server.network.packet.types.recipe;

public class CraftingRecipeBlockName {

    public static final String CARTOGRAPHY_TABLE = "cartography_table";
    public static final String CRAFTING_TABLE = "crafting_table";
    public static final String STONECUTTER = "stonecutter";
    public static final String SMITHING_TABLE = "smithing_table";

    private CraftingRecipeBlockName() {
        throw new UnsupportedOperationException("Cannot construct CraftingRecipeBlockName");
    }
}
