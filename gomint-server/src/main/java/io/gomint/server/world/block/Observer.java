package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemStack;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.world.block.helper.ToolPresets;
import io.gomint.world.block.BlockObserver;
import io.gomint.world.block.BlockType;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:observer")
public class Observer extends Block implements BlockObserver {

    @Override
    public String blockId() {
        return "minecraft:observer";
    }

    @Override
    public long breakTime() {
        return 5250;
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        return ToolPresets.PICKAXE;
    }

    @Override
    public float blastResistance() {
        return 17.5f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.OBSERVER;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

}
