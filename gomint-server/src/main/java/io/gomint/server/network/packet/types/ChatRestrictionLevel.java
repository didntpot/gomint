package io.gomint.server.network.packet.types;

public class ChatRestrictionLevel {

    public static final int NONE = 0;
    public static final int DROPPED = 1;
    public static final int DISABLED = 2;

    private ChatRestrictionLevel() {
        throw new UnsupportedOperationException("Cannot construct ChatRestrictionLevel");
    }
}
