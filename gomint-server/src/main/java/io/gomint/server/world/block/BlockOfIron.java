package io.gomint.server.world.block;

import io.gomint.inventory.item.*;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.world.block.BlockBlockOfIron;
import io.gomint.world.block.BlockType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:iron_block")
public class BlockOfIron extends Block implements BlockBlockOfIron {

    @Override
    public String blockId() {
        return "minecraft:iron_block";
    }

    @Override
    public long breakTime() {
        return 7500;
    }

    @Override
    public float blastResistance() {
        return 10.0f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.BLOCK_OF_IRON;
    }

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        // Only stone, iron and up
        return new Class[]{
            ItemIronPickaxe.class,
            ItemDiamondPickaxe.class,
            ItemStonePickaxe.class
        };
    }

    @Override
    public List<ItemStack<?>> drops(ItemStack<?> itemInHand) {
        if (isCorrectTool(itemInHand)) {
            return new ArrayList<>() {{
                add(ItemBlockOfIron.create(1));
            }};
        }

        return new ArrayList<>();
    }

}
