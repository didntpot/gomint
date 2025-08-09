package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemBlockOfDiamond;
import io.gomint.inventory.item.ItemDiamondPickaxe;
import io.gomint.inventory.item.ItemIronPickaxe;
import io.gomint.inventory.item.ItemStack;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.world.block.BlockBlockOfDiamond;
import io.gomint.world.block.BlockType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:diamond_block")
public class BlockOfDiamond extends Block implements BlockBlockOfDiamond {

    @Override
    public String blockId() {
        return "minecraft:diamond_block";
    }

    @Override
    public long breakTime() {
        return 7500;
    }

    @Override
    public float blastResistance() {
        return 30.0f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.BLOCK_OF_DIAMOND;
    }

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        // Only iron and up
        return new Class[]{
            ItemIronPickaxe.class,
            ItemDiamondPickaxe.class,
        };
    }

    @Override
    public List<ItemStack<?>> drops(ItemStack<?> itemInHand) {
        if (isCorrectTool(itemInHand)) {
            return new ArrayList<>() {{
                add(ItemBlockOfDiamond.create(1));
            }};
        }

        return new ArrayList<>();
    }

}
