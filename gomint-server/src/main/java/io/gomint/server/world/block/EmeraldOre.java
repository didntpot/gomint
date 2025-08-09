package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemEmerald;
import io.gomint.inventory.item.ItemStack;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.world.WorldAdapter;
import io.gomint.server.world.block.helper.ToolPresets;
import io.gomint.world.block.BlockEmeraldOre;
import io.gomint.world.block.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:emerald_ore")
public class EmeraldOre extends Block implements BlockEmeraldOre {

    @Override
    public String blockId() {
        return "minecraft:emerald_ore";
    }

    @Override
    public long breakTime() {
        return 4500;
    }

    @Override
    public float blastResistance() {
        return 15.0f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.EMERALD_ORE;
    }

    @Override
    public boolean canBeBrokenWithHand() {
        return true;
    }

    @Override
    public List<ItemStack<?>> drops(ItemStack<?> itemInHand) {
        List<ItemStack<?>> drops = new ArrayList<>();
        if (isCorrectTool(itemInHand)) {
            ((WorldAdapter) this.location.world()).createExpOrb(this.location, ThreadLocalRandom.current().nextInt(3));
            drops.add(ItemEmerald.create(1));
        }
        return drops;
    }

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        return ToolPresets.PICKAXE;
    }

}
