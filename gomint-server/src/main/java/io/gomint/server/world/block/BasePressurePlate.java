/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.world.block;

import io.gomint.math.AxisAlignedBB;
import io.gomint.math.MathUtils;
import io.gomint.server.entity.Entity;
import io.gomint.server.world.block.state.RedstoneSignalStrength;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author geNAZt
 * @version 1.0
 */
public abstract class BasePressurePlate extends Block {

    private static final RedstoneSignalStrength SIGNAL = new RedstoneSignalStrength(() -> new String[]{"redstone_signal"});

    @Override
    public void stepOn(Entity<?> entity) {
        // Check for additional temporary data
        Integer amountOfEntitiesOn = this.storeInTemporaryStorage("amountOfEntitiesOn", (Function<Integer, Integer>) old -> {
            if (old == null) return 1;
            return old + 1;
        });

        if (amountOfEntitiesOn > 0 && SIGNAL.state(this) <= MathUtils.EPSILON) {
            SIGNAL.on(this);
        }
    }

    @Override
    public void gotOff(Entity<?> entity) {
        Integer amountOfEntitiesOn = this.storeInTemporaryStorage("amountOfEntitiesOn", (Function<Integer, Integer>) old -> {
            // For some weird reason a player can enter and leave a block in the same tick
            if (old == null) return null;

            if (old - 1 == 0) return null;
            return old - 1;
        });

        if (amountOfEntitiesOn == null && SIGNAL.state(this) > MathUtils.EPSILON) {
            SIGNAL.off(this);
        }
    }

    @Override
    public List<AxisAlignedBB> boundingBoxes() {
        return Collections.singletonList(new AxisAlignedBB(
            this.location.x() + 0.0625f,
            this.location.y(),
            this.location.z() + 0.0625f,
            this.location.x() + 0.9375f,
            this.location.y() + 0.0625f,
            this.location.z() + 0.9375f
        ));
    }

}
