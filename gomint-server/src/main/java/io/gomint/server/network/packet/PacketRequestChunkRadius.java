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
public class PacketRequestChunkRadius extends Packet implements PacketServerbound {

    private int chunkRadius;
    private byte maxRadius;

    public PacketRequestChunkRadius() {
        super(Protocol.PACKET_REQUEST_CHUNK_RADIUS);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeSignedVarInt(this.chunkRadius);
        buffer.writeByte(this.maxRadius);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        this.chunkRadius = buffer.readSignedVarInt();
        this.maxRadius = buffer.readByte();
    }

    public int getChunkRadius() {
        return this.chunkRadius;
    }

    public void setChunkRadius(int chunkRadius) {
        this.chunkRadius = chunkRadius;
    }

    public byte getMaxRadius() {
        return this.maxRadius;
    }

    public void setMaxRadius(byte maxRadius) {
        this.maxRadius = maxRadius;
    }
}
