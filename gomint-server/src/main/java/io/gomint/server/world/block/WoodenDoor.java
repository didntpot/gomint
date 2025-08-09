/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.world.block;

import io.gomint.inventory.item.ItemStack;
import io.gomint.inventory.item.ItemWoodenDoor;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.world.block.helper.ToolPresets;
import io.gomint.world.block.BlockType;
import io.gomint.world.block.BlockWoodenDoor;
import io.gomint.world.block.data.Facing;
import io.gomint.world.block.data.HingeSide;
import io.gomint.world.block.data.LogType;

import java.util.Collections;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:wooden_door", def = true)
@RegisterInfo(sId = "minecraft:spruce_door")
@RegisterInfo(sId = "minecraft:birch_door")
@RegisterInfo(sId = "minecraft:jungle_door")
@RegisterInfo(sId = "minecraft:acacia_door")
@RegisterInfo(sId = "minecraft:dark_oak_door")
@RegisterInfo(sId = "minecraft:warped_door")
@RegisterInfo(sId = "minecraft:crimson_door")
public class WoodenDoor extends Door<BlockWoodenDoor> implements BlockWoodenDoor {

    @Override
    public Class<? extends ItemStack<?>>[] toolInterfaces() {
        return ToolPresets.AXE;
    }

    @Override
    public float blastResistance() {
        return 15.0f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.WOODEN_DOOR;
    }

    @Override
    public LogType type() {
        switch (this.blockId()) {
            case "minecraft:crimson_door":
                return LogType.CRIMSON;
            case "minecraft:warped_door":
                return LogType.WARPED;
            case "minecraft:dark_oak_door":
                return LogType.DARK_OAK;
            case "minecraft:acacia_door":
                return LogType.ACACIA;
            case "minecraft:jungle_door":
                return LogType.JUNGLE;
            case "minecraft:birch_door":
                return LogType.BIRCH;
            case "minecraft:spruce_door":
                return LogType.SPRUCE;
            case "minecraft:wooden_door":
            default:
                return LogType.OAK;
        }
    }

    @Override
    public BlockWoodenDoor type(LogType logType) {
        switch (logType) {
            case CRIMSON:
                this.blockId("minecraft:crimson_door");
                break;
            case WARPED:
                this.blockId("minecraft:warped_door");
                break;
            case DARK_OAK:
                this.blockId("minecraft:dark_oak_door");
                break;
            case ACACIA:
                this.blockId("minecraft:acacia_door");
                break;
            case JUNGLE:
                this.blockId("minecraft:jungle_door");
                break;
            case BIRCH:
                this.blockId("minecraft:birch_door");
                break;
            case SPRUCE:
                this.blockId("minecraft:spruce_door");
                break;
            case OAK:
            default:
                this.blockId("minecraft:wooden_door");
        }

        return this;
    }

    @Override
    public List<ItemStack<?>> drops(ItemStack<?> itemInHand) {
        ItemWoodenDoor item = ItemWoodenDoor.create(1);
        item.type(this.type());
        return Collections.singletonList(item);
    }

    @Override
    public void afterPlacement() {
        Block above = this.side(Facing.UP);
        WoodenDoor aDoor = above.blockType(WoodenDoor.class);
        aDoor.direction(this.direction());
        aDoor.top(true);
        aDoor.hingeSide(HingeSide.LEFT);
        aDoor.type(this.type());
        aDoor.open(false);

        super.afterPlacement();
    }

}
