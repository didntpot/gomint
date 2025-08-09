package io.gomint.server.network.type;

import java.util.UUID;

/**
 * @author geNAZt
 * @version 1.0
 */
public class CommandOrigin {

    public static final int ORIGIN_PLAYER = 0;
    public static final int ORIGIN_BLOCK = 1;
    public static final int ORIGIN_MINECART_BLOCK = 2;
    public static final int ORIGIN_DEV_CONSOLE = 3;
    public static final int ORIGIN_TEST = 4;
    public static final int ORIGIN_AUTOMATION_PLAYER = 5;
    public static final int ORIGIN_CLIENT_AUTOMATION = 6;
    public static final int ORIGIN_DEDICATED_SERVER = 7;
    public static final int ORIGIN_ENTITY = 8;
    public static final int ORIGIN_VIRTUAL = 9;
    public static final int ORIGIN_GAME_ARGUMENT = 10;
    public static final int ORIGIN_ENTITY_SERVER = 11; //???

    private int type;
    private UUID uuid;
    private String requestId;
    private long playerEntityUniqueId;

    public CommandOrigin(int type, UUID uuid, String requestId) {
        this.type = type;
        this.uuid = uuid;
        this.requestId = requestId;
    }

    public CommandOrigin(int type, UUID uuid, String requestId, long playerEntityUniqueId) {
        this.type = type;
        this.uuid = uuid;
        this.requestId = requestId;
        this.playerEntityUniqueId = playerEntityUniqueId;
    }

    public String requestId() {
        return this.requestId;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public long playerEntityUniqueId() {
        return this.playerEntityUniqueId;
    }

    public int type() {
        return this.type;
    }

    public CommandOrigin requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public CommandOrigin uuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public CommandOrigin playerEntityUniqueId(byte playerEntityUniqueId) {
        this.playerEntityUniqueId = playerEntityUniqueId;
        return this;
    }

    public CommandOrigin type(byte type) {
        this.type = type;
        return this;
    }
}
