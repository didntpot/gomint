package io.gomint.server.network.packet.types;

public class CompressionAlgorithm {

    private CompressionAlgorithm() {
        throw new UnsupportedOperationException("Cannot construct CompressionAlgorithm");
    }

    public static final short ZLIB = 0;
    public static final short SNAPPY = 1;
}
