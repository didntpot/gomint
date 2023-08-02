/*
 * Copyright (c) 2020 Gomint team
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
public class PacketNetworkChunkPublisherUpdate extends Packet implements PacketClientbound {

    private BlockPosition blockPosition;
    private int radius;
    private ChunkPosition[] savedChunks;

    /**
     * Construct a new packet
     */
    public PacketNetworkChunkPublisherUpdate() {
        super(Protocol.PACKET_NETWORK_CHUNK_PUBLISHER_UPDATE);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) throws Exception {
        writeSignedBlockPosition(this.blockPosition, buffer);
        buffer.writeUnsignedVarInt(this.radius);

        buffer.writeLInt(this.savedChunks.length);
        for (ChunkPosition savedChunk : this.savedChunks) {
            savedChunk.write(buffer);
        }
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {
        readSignedBlockPosition(buffer);
        buffer.readUnsignedVarInt();
    }

    public BlockPosition getBlockPosition() {
        return this.blockPosition;
    }

    public void setBlockPosition(BlockPosition blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ChunkPosition[] getSavedChunks() {
        return this.savedChunks;
    }

    public void setSavedChunks(ChunkPosition[] savedChunks) {
        this.savedChunks = savedChunks;
    }

    public static class ChunkPosition {
        private int x;
        private int z;

        public ChunkPosition(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public static ChunkPosition read(PacketBuffer buffer) {
            return new ChunkPosition(buffer.readSignedVarInt(), buffer.readSignedVarInt());
        }

        public void write(PacketBuffer buffer) {
            buffer.writeSignedVarInt(this.x);
            buffer.writeSignedVarInt(this.z);
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }
    }
}
