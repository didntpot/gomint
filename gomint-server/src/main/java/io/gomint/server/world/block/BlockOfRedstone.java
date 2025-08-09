package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemBlockOfRedstone;
import io.gomint.inventory.item.ItemStack;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.world.block.helper.ToolPresets;
import io.gomint.world.block.BlockBlockOfRedstone;
import io.gomint.world.block.BlockType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:redstone_block")
public class BlockOfRedstone extends Block implements BlockBlockOfRedstone {

    @Override
    public String blockId() {
        return "minecraft:redstone_block";
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
        return BlockType.BLOCK_OF_REDSTONE;
    }

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        return ToolPresets.PICKAXE;
    }

    @Override
    public List<ItemStack<?>> drops(ItemStack<?> itemInHand) {
        if (isCorrectTool(itemInHand)) {
            return new ArrayList<>() {{
                add(ItemBlockOfRedstone.create(1));
            }};
        }

        return new ArrayList<>();
    }

}
