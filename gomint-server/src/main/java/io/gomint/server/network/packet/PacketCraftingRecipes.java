/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.types.recipe.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BlackyPaw
 * @version 1.0
 */
public class PacketCraftingRecipes extends Packet implements PacketClientbound {
    public static final int ENTRY_SHAPELESS = 0;
    public static final int ENTRY_SHAPED = 1;
    public static final int ENTRY_FURNACE = 2;
    public static final int ENTRY_FURNACE_DATA = 3;
    public static final int ENTRY_MULTI = 4;
    public static final int ENTRY_SHULKER_BOX = 5;
    public static final int ENTRY_SHAPELESS_CHEMISTRY = 6;
    public static final int ENTRY_SHAPED_CHEMISTRY = 7;
    public static final int ENTRY_SMITHING_TRANSFORM = 8;
    public static final int ENTRY_SMITHING_TRIM = 9;

    private List<RecipeWithTypeId> recipesWithTypeIds = new ArrayList<>();
    private List<PotionTypeRecipe> potionTypeRecipes = new ArrayList<>();
    private List<PotionContainerChangeRecipe> potionContainerChangeRecipes = new ArrayList<>();
    private List<MaterialReducerRecipe> materialReducerRecipes = new ArrayList<>();
    private boolean cleanRecipes = false;


    /**
     * Construct new crafting recipe packet
     */
    public PacketCraftingRecipes() {
        super(Protocol.PACKET_CRAFTING_RECIPES);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeUnsignedVarInt(recipesWithTypeIds.size());
        for (RecipeWithTypeId recipe : recipesWithTypeIds) {
            buffer.writeSignedVarInt(recipe.getTypeId());
            recipe.encode(buffer);
        }
        buffer.writeUnsignedVarInt(potionTypeRecipes.size());
        for (PotionTypeRecipe recipe : potionTypeRecipes) {
            buffer.writeSignedVarInt(recipe.getInputItemId());
            buffer.writeSignedVarInt(recipe.getInputItemMeta());
            buffer.writeSignedVarInt(recipe.getIngredientItemId());
            buffer.writeSignedVarInt(recipe.getIngredientItemMeta());
            buffer.writeSignedVarInt(recipe.getOutputItemId());
            buffer.writeSignedVarInt(recipe.getOutputItemMeta());
        }
        buffer.writeUnsignedVarInt(potionContainerChangeRecipes.size());
        for (PotionContainerChangeRecipe recipe : potionContainerChangeRecipes) {
            buffer.writeSignedVarInt(recipe.getInputItemId());
            buffer.writeSignedVarInt(recipe.getIngredientItemId());
            buffer.writeSignedVarInt(recipe.getOutputItemId());
        }
        buffer.writeUnsignedVarInt(materialReducerRecipes.size());
        for (MaterialReducerRecipe recipe : materialReducerRecipes) {
            buffer.writeSignedVarInt((recipe.getInputItemId() << 16) | recipe.getInputItemMeta());
            buffer.writeUnsignedVarInt(recipe.getOutputs().size());
            for (MaterialReducerRecipeOutput output : recipe.getOutputs()) {
                buffer.writeSignedVarInt(output.getItemId());
                buffer.writeSignedVarInt(output.getCount());
            }
        }
        buffer.writeBoolean(cleanRecipes);
    }

    public void setRecipesWithTypeIds(List<RecipeWithTypeId> recipesWithTypeIds) {
        this.recipesWithTypeIds = recipesWithTypeIds;
    }

    public void setPotionTypeRecipes(List<PotionTypeRecipe> potionTypeRecipes) {
        this.potionTypeRecipes = potionTypeRecipes;
    }

    public void setPotionContainerChangeRecipes(List<PotionContainerChangeRecipe> potionContainerChangeRecipes) {
        this.potionContainerChangeRecipes = potionContainerChangeRecipes;
    }

    public void setMaterialReducerRecipes(List<MaterialReducerRecipe> materialReducerRecipes) {
        this.materialReducerRecipes = materialReducerRecipes;
    }

    public void setCleanRecipes(boolean cleanRecipes) {
        this.cleanRecipes = cleanRecipes;
    }

    public List<RecipeWithTypeId> getRecipesWithTypeIds() {
        return recipesWithTypeIds;
    }

    public List<PotionTypeRecipe> getPotionTypeRecipes() {
        return potionTypeRecipes;
    }

    public List<PotionContainerChangeRecipe> getPotionContainerChangeRecipes() {
        return potionContainerChangeRecipes;
    }

    public List<MaterialReducerRecipe> getMaterialReducerRecipes() {
        return materialReducerRecipes;
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {

    }
}
