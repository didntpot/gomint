package io.gomint.server.network.packet.types;

public class PlayerPermission {

    public static final int CUSTOM = 3;
    public static final int OPERATOR = 2;
    public static final int MEMBER = 1;
    public static final int VISITOR = 0;

    private PlayerPermission() {
        throw new UnsupportedOperationException("Cannot construct PlayerPermission");
    }
}
