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
public class PacketHurtArmor extends Packet implements PacketClientbound {

    private int cause;
    private int damage;
    private int armorSlotFlags;

    /**
     * Create new packet entity event
     */
    public PacketHurtArmor() {
        super(Protocol.PACKET_HURT_ARMOR);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeSignedVarInt(this.cause);
        buffer.writeSignedVarInt(this.damage);
        buffer.writeUnsignedVarLong(this.armorSlotFlags);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        this.cause = buffer.readSignedVarInt();
        this.damage = buffer.readSignedVarInt();
        this.armorSlotFlags = (int) buffer.readUnsignedVarLong();
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getCause() {
        return this.cause;
    }

    public void setCause(int cause) {
        this.cause = cause;
    }

    public int getArmorSlotFlags() {
        return this.armorSlotFlags;
    }

    public void setArmorSlotFlags(int armorSlotFlags) {
        this.armorSlotFlags = armorSlotFlags;
    }
}
