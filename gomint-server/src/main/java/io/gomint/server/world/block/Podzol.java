package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemDirt;
import io.gomint.inventory.item.ItemStack;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.world.block.helper.ToolPresets;
import io.gomint.world.block.BlockPodzol;
import io.gomint.world.block.BlockType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:podzol")
public class Podzol extends Block implements BlockPodzol {

    @Override
    public String blockId() {
        return "minecraft:podzol";
    }

    @Override
    public long breakTime() {
        return 750;
    }

    @Override
    public float blastResistance() {
        return 2.5f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.PODZOL;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

    @Override
    public List<ItemStack<?>> drops(ItemStack<?> itemInHand) {
        return new ArrayList<>() {{
            add(ItemDirt.create(1));
        }};
    }

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        return ToolPresets.SHOVEL;
    }

}
