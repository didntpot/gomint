package io.gomint.server.inventory.item;

import io.gomint.inventory.item.ItemType;
import io.gomint.math.Vector;
import io.gomint.server.entity.Attribute;
import io.gomint.server.entity.AttributeModifier;
import io.gomint.server.entity.AttributeModifierType;
import io.gomint.server.entity.EntityPlayer;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.world.block.Dirt;
import io.gomint.server.world.block.Farmland;
import io.gomint.server.world.block.GrassBlock;
import io.gomint.world.block.Block;
import io.gomint.world.block.data.Facing;

import java.time.Duration;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:wooden_hoe")
public class ItemWoodenHoe extends ItemReduceTierWooden<io.gomint.inventory.item.ItemWoodenHoe> implements io.gomint.inventory.item.ItemWoodenHoe {

    @Override
    public Duration burnTime() {
        return Duration.ofMillis(10000);
    }

    @Override
    public boolean interact(EntityPlayer entity, Facing face, Vector clickPosition, Block clickedBlock) {
        if (clickedBlock instanceof Dirt || clickedBlock instanceof GrassBlock) {
            clickedBlock.blockType(Farmland.class);
            this.calculateUsageAndUpdate(1);
            return true;
        }

        return false;
    }

    @Override
    public void gotInHand(EntityPlayer player) {
        player
            .attributeInstance(Attribute.ATTACK_DAMAGE)
            .setModifier(AttributeModifier.ITEM_ATTACK_DAMAGE, AttributeModifierType.ADDITION, 1);
    }

    @Override
    public void removeFromHand(EntityPlayer player) {
        player
            .attributeInstance(Attribute.ATTACK_DAMAGE)
            .removeModifier(AttributeModifier.ITEM_ATTACK_DAMAGE);
    }

    @Override
    public ItemType itemType() {
        return ItemType.WOODEN_HOE;
    }

}
