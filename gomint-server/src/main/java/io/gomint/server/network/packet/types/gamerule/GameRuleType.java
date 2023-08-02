package io.gomint.server.network.packet.types.gamerule;

public class GameRuleType {

    public static final int BOOLEAN = 1;
    public static final int INTEGER = 2;
    public static final int FLOAT = 3;

    private GameRuleType() {
        throw new UnsupportedOperationException("Cannot construct GameRuleType");
    }
}
