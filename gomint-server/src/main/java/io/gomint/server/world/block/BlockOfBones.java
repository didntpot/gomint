/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemBlockOfBones;
import io.gomint.inventory.item.ItemStack;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.world.block.helper.ToolPresets;
import io.gomint.world.block.BlockType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:bone_block")
public class BlockOfBones extends Block implements io.gomint.world.block.BlockOfBones {

    @Override
    public String blockId() {
        return "minecraft:bone_block";
    }

    @Override
    public long breakTime() {
        return 3000;
    }

    @Override
    public float blastResistance() {
        return 10f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.BLOCK_OF_BONES;
    }

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        return ToolPresets.PICKAXE;
    }

    @Override
    public List<ItemStack<?>> drops(ItemStack<?> itemInHand) {
        if (isCorrectTool(itemInHand)) {
            return new ArrayList<>() {{
                add(ItemBlockOfBones.create(1));
            }};
        }

        return new ArrayList<>();
    }

}
