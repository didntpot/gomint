/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.math.BlockPosition;
import io.gomint.server.network.Protocol;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketBlockEvent extends Packet implements PacketClientbound {

    private BlockPosition position;
    private int eventType;
    private int eventData;

    /**
     * Construct a new packet
     */
    public PacketBlockEvent() {
        super(Protocol.PACKET_BLOCK_EVENT);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        writeBlockPosition(this.position, buffer);
        buffer.writeSignedVarInt(this.eventType);
        buffer.writeSignedVarInt(this.eventData);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {

    }

    public BlockPosition getPosition() {
        return this.position;
    }

    public void setPosition(BlockPosition position) {
        this.position = position;
    }

    public int getEventType() {
        return this.eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getEventData() {
        return this.eventData;
    }

    public void setEventData(int eventData) {
        this.eventData = eventData;
    }
}
