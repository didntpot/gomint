/*
 * Copyright (c) 2020 Gomint team
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint;

import io.gomint.server.world.NibbleArray;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Fork(value = 1, warmups = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class NibbleBenchmark {

    private final NibbleArray array = new NibbleArray((short) 4096);
    private final short index = getIndex(4, 3, 5);

    private short getIndex(int x, int y, int z) {
        return (short) ((x << 8) + (z << 4) + y);
    }

    @Benchmark
    public void bench() {
        this.array.set(this.index, (byte) 255);
    }

}
