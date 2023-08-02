package io.gomint.server.inventory.item;

import io.gomint.inventory.item.ItemType;
import io.gomint.server.registry.RegisterInfo;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:sandstone")
public class ItemSandstone extends ItemStack<io.gomint.inventory.item.ItemSandstone> implements io.gomint.inventory.item.ItemSandstone {

    @Override
    public ItemType itemType() {
        return ItemType.SANDSTONE;
    }

}
