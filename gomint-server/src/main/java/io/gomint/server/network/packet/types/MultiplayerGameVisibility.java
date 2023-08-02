package io.gomint.server.network.packet.types;

public class MultiplayerGameVisibility {

    public static final int NONE = 0;
    public static final int INVITE = 1;
    public static final int FRIENDS = 2;
    public static final int FRIENDS_OF_FRIEND = 3;
    public static final int PUBLIC = 4;

    private MultiplayerGameVisibility() {
        throw new UnsupportedOperationException("Cannot construct MultiplayerGameVisibility");
    }
}
