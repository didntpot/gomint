package io.gomint.server.network.packet.util;

public class PacketDecodeException extends RuntimeException {
    public PacketDecodeException(String message) {
        super(message);
    }

    public PacketDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketDecodeException(Throwable cause) {
        super(cause);
    }
}
