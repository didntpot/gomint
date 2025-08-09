package io.gomint.server.network.packet.types.ability;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.packet.util.PacketDecodeException;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class AbilitiesLayer {
    public static final int LAYER_CACHE = 0;
    public static final int LAYER_BASE = 1;
    public static final int LAYER_SPECTATOR = 2;
    public static final int LAYER_COMMANDS = 3;
    public static final int LAYER_EDITOR = 4;

    public static final int ABILITY_BUILD = 0;
    public static final int ABILITY_MINE = 1;
    public static final int ABILITY_DOORS_AND_SWITCHES = 2; //disabling this also disables dropping items (???)
    public static final int ABILITY_OPEN_CONTAINERS = 3;
    public static final int ABILITY_ATTACK_PLAYERS = 4;
    public static final int ABILITY_ATTACK_MOBS = 5;
    public static final int ABILITY_OPERATOR = 6;
    public static final int ABILITY_TELEPORT = 7;
    public static final int ABILITY_INVULNERABLE = 8;
    public static final int ABILITY_FLYING = 9;
    public static final int ABILITY_ALLOW_FLIGHT = 10;
    public static final int ABILITY_INFINITE_RESOURCES = 11; //in vanilla they call this "instabuild", which is a bad name
    public static final int ABILITY_LIGHTNING = 12; //???
    private static final int ABILITY_FLY_SPEED = 13;
    private static final int ABILITY_WALK_SPEED = 14;
    public static final int ABILITY_MUTED = 15;
    public static final int ABILITY_WORLD_BUILDER = 16;
    public static final int ABILITY_NO_CLIP = 17;
    public static final int ABILITY_PRIVILEGED_BUILDER = 18;

    public static final int NUMBER_OF_ABILITIES = 19;

    private short layerId;
    private Map<Integer, Boolean> booleanAbilities;
    private @Nullable Float flySpeed;
    private @Nullable Float walkSpeed;

    public AbilitiesLayer(short layerId, Map<Integer, Boolean> booleanAbilities, @Nullable float flySpeed, @Nullable float walkSpeed) {
        this.layerId = layerId;
        this.booleanAbilities = booleanAbilities;
        this.flySpeed = flySpeed;
        this.walkSpeed = walkSpeed;
    }

    public static AbilitiesLayer decode(PacketBuffer buffer) {
        short layerId = buffer.readLShort();
        int setAbilities = buffer.readLInt();
        int setAbilitiesValues = buffer.readLInt();
        Float flySpeed = buffer.readLFloat();
        Float walkSpeed = buffer.readLFloat();

        Map<Integer, Boolean> booleanAbilities = new HashMap<>();
        for (int i = 0; i < NUMBER_OF_ABILITIES; i++) {
            if (i == ABILITY_FLY_SPEED || i == ABILITY_WALK_SPEED) {
                continue;
            }
            if ((setAbilities & (1 << i)) != 0) {
                booleanAbilities.put(i, (setAbilitiesValues & (1 << i)) != 0);
            }
        }
        if ((setAbilities & (1 << ABILITY_FLY_SPEED)) == 0) {
            if (flySpeed != 0.0) {
                throw new PacketDecodeException("Fly speed should be zero if the layer does not set it");
            }
            flySpeed = null;
        }
        if ((setAbilities & (1 << ABILITY_WALK_SPEED)) == 0) {
            if (walkSpeed != 0.0) {
                throw new PacketDecodeException("Walk speed should be zero if the layer does not set it");
            }
            walkSpeed = null;
        }
        return new AbilitiesLayer(layerId, booleanAbilities, flySpeed, walkSpeed);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeLShort(this.layerId);
        int setAbilities = 0;
        int setAbilitiesValues = 0;
        for (Map.Entry<Integer, Boolean> entry : this.booleanAbilities.entrySet()) {
            setAbilities |= 1 << entry.getKey();
            setAbilitiesValues |= (entry.getValue() ? 1 << entry.getKey() : 0);
        }
        if (this.flySpeed != null) {
            setAbilities |= 1 << ABILITY_FLY_SPEED;
        }
        if (this.walkSpeed != null) {
            setAbilities |= 1 << ABILITY_WALK_SPEED;
        }
        buffer.writeLInt(setAbilities);
        buffer.writeLInt(setAbilitiesValues);
        buffer.writeLFloat(this.flySpeed == null ? 0.0f : this.flySpeed);
        buffer.writeLFloat(this.walkSpeed == null ? 0.0f : this.walkSpeed);
    }

    public short getLayerId() {
        return layerId;
    }

    public static int getAbilityFlySpeed() {
        return ABILITY_FLY_SPEED;
    }

    public static int getAbilityWalkSpeed() {
        return ABILITY_WALK_SPEED;
    }

    @Nullable
    public Float getFlySpeed() {
        return flySpeed;
    }

    @Nullable
    public Float getWalkSpeed() {
        return walkSpeed;
    }

    public Map<Integer, Boolean> getBooleanAbilities() {
        return booleanAbilities;
    }

    public void setBooleanAbilities(Map<Integer, Boolean> booleanAbilities) {
        this.booleanAbilities = booleanAbilities;
    }

    public void setFlySpeed(@Nullable Float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setLayerId(short layerId) {
        this.layerId = layerId;
    }

    public void setWalkSpeed(@Nullable Float walkSpeed) {
        this.walkSpeed = walkSpeed;
    }
}
