/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.entity.projectile;

import io.gomint.event.entity.EntityDamageEvent;
import io.gomint.math.Location;
import io.gomint.math.MathUtils;
import io.gomint.server.entity.EntityLiving;
import io.gomint.server.entity.EntityType;
import io.gomint.server.registry.RegisterInfo;
import io.gomint.server.util.Values;
import io.gomint.server.world.LevelEvent;
import io.gomint.server.world.WorldAdapter;
import io.gomint.world.Particle;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author geNAZt
 * @version 1.0
 */
@RegisterInfo(sId = "minecraft:xp_bottle")
public class EntityExpBottle extends EntityThrowable<io.gomint.entity.projectile.EntityExpBottle> implements io.gomint.entity.projectile.EntityExpBottle {

    private float lastUpdateDT;

    /**
     * Construct a new Entity
     *
     * @param shooter of this entity
     * @param world   The world in which this entity is in
     */
    public EntityExpBottle(EntityLiving<?> shooter, WorldAdapter world) {
        super(shooter, EntityType.EXP_BOTTLE_PROJECTILE, world);

        // Get position from the shooter
        Location position = this.positionFromShooter();

        // Calculate motion
        this.motionFromEntity(position, this.shooter.velocity(), -20f, 0.7f, 1f);

        // Calculate correct yaw / pitch
        this.lookFromMotion();
    }

    /**
     * Create entity for API
     */
    public EntityExpBottle() {
        super(null, EntityType.EXP_BOTTLE_PROJECTILE, null);
    }

    @Override
    protected void applyCustomProperties() {
        super.applyCustomProperties();

        // Gravity
        this.gravity = 0.07f;
        this.drag = 0.01f;
    }

    @Override
    public void update(long currentTimeMS, float dT) {
        super.update(currentTimeMS, dT);

        if (this.hitEntity != null) {
            this.breakBottle();
            this.despawn();
        }

        this.lastUpdateDT += dT;
        if (Values.CLIENT_TICK_RATE - this.lastUpdateDT < MathUtils.EPSILON) {
            if (this.isCollided) {
                this.breakBottle();
                this.despawn();
            }

            // Despawn after 1200 ticks ( 1 minute )
            if (this.age >= 1200) {
                this.despawn();
            }

            this.lastUpdateDT = 0;
        }
    }

    private void breakBottle() {
        Location location = this.location();
        this.world.sendParticle(location, Particle.MOB_SPELL);
        this.world.sendLevelEvent(location, LevelEvent.PARTICLE_SPLASH, 0x00385dc6);

        int amountOfOrbs = 3 + ThreadLocalRandom.current().nextInt(8);
        int add = 1;
        for (int i = 0; i < amountOfOrbs; i += add) {
            this.world.createExpOrb(location, add);
            add = 1 + ThreadLocalRandom.current().nextInt(2);
        }
    }

    @Override
    public boolean damage(EntityDamageEvent damageEvent) {
        return (damageEvent.damageSource() == EntityDamageEvent.DamageSource.VOID ||
            damageEvent.damageSource() == EntityDamageEvent.DamageSource.ON_FIRE ||
            damageEvent.damageSource() == EntityDamageEvent.DamageSource.ENTITY_EXPLODE)
            && super.damage(damageEvent);
    }

}
