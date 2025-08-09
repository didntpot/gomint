package io.gomint.server.world.block;

import io.gomint.event.entity.EntityDamageEvent;
import io.gomint.server.entity.EntityLiving;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.world.block.BlockFlowingLava;
import io.gomint.world.block.BlockType;
import io.gomint.world.block.data.Facing;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:flowing_lava")
public class FlowingLava extends Liquid<BlockFlowingLava> implements BlockFlowingLava {

    @Override
    public long breakTime() {
        return 150000;
    }

    @Override
    public boolean transparent() {
        return true;
    }

    @Override
    public boolean solid() {
        return false;
    }

    @Override
    public int getTickDiff() {
        return 1500; // Depends on the world, in nether its 10 ticks / otherwise its 30
    }

    @Override
    public boolean isFlowing() {
        return true;
    }

    @Override
    public void onEntityStanding(EntityLiving<?> entityLiving) {
        EntityDamageEvent damageEvent = new EntityDamageEvent(entityLiving, EntityDamageEvent.DamageSource.LAVA, 4.0f);
        entityLiving.damage(damageEvent);
        entityLiving.burning(15, TimeUnit.SECONDS);
        entityLiving.multiplyFallDistance(0.5f);
    }

    @Override
    protected byte getFlowDecayPerBlock() {
        return 2;
    }

    @Override
    public float blastResistance() {
        return 500.0f;
    }

    @Override
    public BlockType blockType() {
        return BlockType.FLOWING_LAVA;
    }

    @Override
    protected void checkForHarden() {
        // Find the block side on which we collided, can be anything except downwards
        Block colliding = null;
        for (Facing blockFace : Facing.values()) {
            if (blockFace == Facing.DOWN) {
                continue;
            }

            Block otherBlock = this.side(blockFace);
            if (otherBlock.blockType() == BlockType.FLOWING_WATER || otherBlock.blockType() == BlockType.STATIONARY_WATER) {
                colliding = otherBlock;
                break;
            }
        }

        // Did we find a block we can collide with?
        if (colliding != null) {
            if (this.fillHeight() > 4 || colliding.blockType() == BlockType.STATIONARY_WATER) {
                this.liquidCollide(colliding, Obsidian.class);
            } else if (this.fillHeight() <= 4) {
                this.liquidCollide(colliding, Cobblestone.class);
            }
        }
    }

    @Override
    protected void flowIntoBlock(Block block, int newFlowDecay) {
        if (block.blockType() == BlockType.FLOWING_WATER) {
            ((Liquid<?>) block).liquidCollide(this, Stone.class);
        } else {
            super.flowIntoBlock(block, newFlowDecay);
        }
    }

}
