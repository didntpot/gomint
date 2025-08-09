package io.gomint.server.world.block;

import io.gomint.event.entity.EntityDamageEvent;
import io.gomint.server.entity.EntityLiving;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.world.block.BlockStationaryLava;
import io.gomint.world.block.BlockType;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:lava")
public class StationaryLava extends Liquid<BlockStationaryLava> implements BlockStationaryLava {

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
    public float fillHeight() {
        return 1f;
    }

    @Override
    public int getTickDiff() {
        return 1500; // Depends on the world, in nether its 10 ticks / otherwise its 30
    }

    @Override
    public boolean isFlowing() {
        return false;
    }

    @Override
    public void onEntityStanding(EntityLiving<?> entityLiving) {
        entityLiving.attack(4.0f, EntityDamageEvent.DamageSource.LAVA);
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
        return BlockType.STATIONARY_LAVA;
    }

}
