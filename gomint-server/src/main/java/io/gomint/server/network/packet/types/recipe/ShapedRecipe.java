package io.gomint.server.network.packet.types.recipe;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.inventory.item.ItemStack;
import io.gomint.server.network.packet.Packet;

import java.util.*;

public class ShapedRecipe extends RecipeWithTypeId {

    private String recipeId;
    private Map<Integer, Map<Integer, RecipeIngredient>> input;
    private List<ItemStack<?>> output;
    private UUID uuid;
    private String blockType;
    private int priority;
    private int recipeNetId;

    public ShapedRecipe(
        int typeId,
        String recipeId,
        Map<Integer, Map<Integer, RecipeIngredient>> input,
        List<ItemStack<?>> output,
        UUID uuid,
        String blockType,
        int priority,
        int recipeNetId
    ) {
        super(typeId);
        int rows = input.size();
        if (rows < 1 || rows > 3) {
            throw new IllegalArgumentException("Expected 1, 2, or 3 input rows");
        }
        Integer columns = null;

        for (Map.Entry<Integer, Map<Integer, RecipeIngredient>> row : input.entrySet()) {
            if (columns == null) {
                columns = row.getValue().size();
            } else if (row.getValue().size() != columns) {
                throw new IllegalArgumentException("Expected each row to be " + columns + " columns, but have " + row.getValue().size() + " in row " + row.getKey());
            }
        }

        this.recipeId = recipeId;
        this.input = input;
        this.output = output;
        this.uuid = uuid;
        this.blockType = blockType;
        this.priority = priority;
        this.recipeNetId = recipeNetId;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    public Map<Integer, Map<Integer, RecipeIngredient>> getInput() {
        return this.input;
    }

    public List<ItemStack<?>> getOutput() {
        return this.output;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getBlockType() {
        return this.blockType;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getRecipeNetId() {
        return this.recipeNetId;
    }

    public int getWidth() {
        return input.get(0).size();
    }

    public int getHeight() {
        return input.size();
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeString(this.recipeId);
        buffer.writeSignedVarInt(this.getWidth());
        buffer.writeSignedVarInt(this.getHeight());
        for (Map.Entry<Integer, Map<Integer, RecipeIngredient>> entry : this.input.entrySet()) {
            for (Map.Entry<Integer, RecipeIngredient> ingredient : entry.getValue().entrySet()) {
                Packet.writeRecipeIngredient(ingredient.getValue(), buffer);
            }
        }

        buffer.writeUnsignedVarInt(this.output.size());
        for (ItemStack<?> itemStack : this.output) {
            Packet.writeItemStack(itemStack, buffer);
        }

        buffer.writeUUID(this.uuid);
        buffer.writeString(this.blockType);
        buffer.writeSignedVarInt(this.priority);
        buffer.writeSignedVarInt(this.recipeNetId);
    }

    public static ShapedRecipe decode(int recipeType, PacketBuffer buffer) {
        String recipeId = buffer.readString();
        int width = buffer.readSignedVarInt();
        int height = buffer.readSignedVarInt();
        Map<Integer, Map<Integer, RecipeIngredient>> input = new HashMap<>();
        for (int y = 0; y < height; y++) {
            Map<Integer, RecipeIngredient> row = new HashMap<>();
            for (int x = 0; x < width; x++) {
                row.put(x, Packet.getRecipeIngredient(buffer));
            }
            input.put(y, row);
        }

        int outputCount = buffer.readUnsignedVarInt();
        List<ItemStack<?>> output = new ArrayList<>(outputCount);
        for (int i = 0; i < outputCount; i++) {
            output.add((ItemStack<?>) Packet.readItemStack(buffer));
        }
        UUID uuid = buffer.readUUID();
        String blockType = buffer.readString();
        int priority = buffer.readSignedVarInt();
        int recipeNetId = buffer.readSignedVarInt();

        return new ShapedRecipe(recipeType, recipeId, input, output, uuid, blockType, priority, recipeNetId);
    }
}
