/*
 * Copyright (c) 2020 Gomint team
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint;

import io.gomint.server.util.performance.ASMFactoryConstructionFactory;
import io.gomint.server.util.performance.ConstructionFactory;
import io.gomint.server.util.performance.LambdaConstructionFactory;
import io.gomint.server.util.performance.ObjectConstructionFactory;
import io.gomint.server.world.block.Air;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(value = 1, warmups = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class ConstructionFactoryBenchmark {

    private final ObjectConstructionFactory<?> objectConstructionFactory = new ObjectConstructionFactory<>(Air.class);
    private final LambdaConstructionFactory<?> lambdaConstructionFactory = new LambdaConstructionFactory<>(Air.class);
    private final ConstructionFactory<?> asmConstructionFactory = ASMFactoryConstructionFactory.create(Air.class);

    @Benchmark
    public Air lambdaConstruction() {
        return (Air) this.lambdaConstructionFactory.newInstance();
    }

    @Benchmark
    public Air objectConstruction() {
        return (Air) this.objectConstructionFactory.newInstance();
    }

    @Benchmark
    public Air asmConstruction() {
        return (Air) this.asmConstructionFactory.newInstance();
    }

}
