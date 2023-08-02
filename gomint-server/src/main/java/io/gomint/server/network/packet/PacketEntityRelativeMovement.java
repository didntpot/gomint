/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketEntityRelativeMovement extends Packet implements PacketClientbound {

    public static final int FLAG_HAS_X = 0x01;
    public static final int FLAG_HAS_Y = 0x02;
    public static final int FLAG_HAS_Z = 0x04;
    public static final int FLAG_HAS_PITCH = 0x08;
    public static final int FLAG_HAS_YAW = 0x10;
    public static final int FLAG_HAS_HEAD_YAW = 0x20;
    public static final int FLAG_GROUND = 0x40;
    public static final int FLAG_TELEPORT = 0x80;
    public static final int FLAG_FORCE_MOVE_LOCAL_ENTITY = 0x100;

    private long entityId;
    private short flags;

    private float oldX;
    private float oldY;
    private float oldZ;

    private float x;
    private float y;
    private float z;

    private float oldPitch;
    private float oldYaw;
    private float oldHeadYaw;

    private float pitch;
    private float yaw;
    private float headYaw;

    /**
     * Construct a new packet
     */
    public PacketEntityRelativeMovement() {
        super(Protocol.PACKET_ENTITY_RELATIVE_MOVEMENT);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeUnsignedVarLong(this.entityId);

        short flags = 0;
        if (this.x != this.oldX) {
            flags |= FLAG_HAS_X;
        }

        if (this.y != this.oldY) {
            flags |= FLAG_HAS_Y;
        }

        if (this.z != this.oldZ) {
            flags |= FLAG_HAS_Z;
        }

        if (this.pitch != this.oldPitch) {
            flags |= FLAG_HAS_PITCH;
        }

        if (this.headYaw != this.oldHeadYaw) {
            flags |= FLAG_HAS_YAW;
        }

        if (this.yaw != this.oldYaw) {
            flags |= FLAG_HAS_HEAD_YAW;
        }

        buffer.writeLShort(flags);

        if (this.x != this.oldX) {
            buffer.writeLFloat(this.x);
        }

        if (this.y != this.oldY) {
            buffer.writeLFloat(this.y);
        }

        if (this.z != this.oldZ) {
            buffer.writeLFloat(this.z);
        }

        if (this.pitch != this.oldPitch) {
            writeByteRotation(this.pitch, buffer);
        }

        if (this.headYaw != this.oldHeadYaw) {
            writeByteRotation(this.headYaw, buffer);
        }

        if (this.yaw != this.oldYaw) {
            writeByteRotation(this.yaw, buffer);
        }
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        this.entityId = buffer.readUnsignedVarLong();
        this.flags = buffer.readLShort();

        if ((this.flags & 1) == 1) {
            this.x = buffer.readLFloat();
        }

        if ((this.flags & 2) == 2) {
            this.y = buffer.readLFloat();
        }

        if ((this.flags & 4) == 4) {
            this.z = buffer.readLFloat();
        }

        if ((this.flags & 8) == 8) {
            this.pitch = readByteRotation(buffer);
        }

        if ((this.flags & 16) == 16) {
            this.headYaw = readByteRotation(buffer);
        }

        if ((this.flags & 32) == 32) {
            this.yaw = readByteRotation(buffer);
        }
    }

    public long getEntityId() {
        return this.entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public short getFlags() {
        return this.flags;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public float getOldX() {
        return this.oldX;
    }

    public void setOldX(float oldX) {
        this.oldX = oldX;
    }

    public float getOldY() {
        return this.oldY;
    }

    public void setOldY(float oldY) {
        this.oldY = oldY;
    }

    public float getOldZ() {
        return this.oldZ;
    }

    public void setOldZ(float oldZ) {
        this.oldZ = oldZ;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getOldPitch() {
        return this.oldPitch;
    }

    public void setOldPitch(float oldPitch) {
        this.oldPitch = oldPitch;
    }

    public float getOldYaw() {
        return this.oldYaw;
    }

    public void setOldYaw(float oldYaw) {
        this.oldYaw = oldYaw;
    }

    public float getOldHeadYaw() {
        return this.oldHeadYaw;
    }

    public void setOldHeadYaw(float oldHeadYaw) {
        this.oldHeadYaw = oldHeadYaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getHeadYaw() {
        return this.headYaw;
    }

    public void setHeadYaw(float headYaw) {
        this.headYaw = headYaw;
    }

}
