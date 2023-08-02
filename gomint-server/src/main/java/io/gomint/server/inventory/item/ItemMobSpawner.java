package io.gomint.server.inventory.item;

import io.gomint.inventory.item.ItemType;
import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:mob_spawner")
public class ItemMobSpawner extends ItemStack<io.gomint.inventory.item.ItemMobSpawner> implements io.gomint.inventory.item.ItemMobSpawner {

    @Override
    public ItemType itemType() {
        return ItemType.MOB_SPAWNER;
    }

}
