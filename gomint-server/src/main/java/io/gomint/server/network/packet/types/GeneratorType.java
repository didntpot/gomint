package io.gomint.server.network.packet.types;

public class GeneratorType {

    public static final int FINITE_OVERWORLD = 0;
    public static final int OVERWORLD = 1;
    public static final int FLAT = 2;
    public static final int NETHER = 3;
    public static final int THE_END = 4;

    private GeneratorType() {
        throw new UnsupportedOperationException("Cannot construct GeneratorType");
    }
}
