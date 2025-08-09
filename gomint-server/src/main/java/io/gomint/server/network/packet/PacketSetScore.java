/*
 * Copyright (c) 2020, GoMint, BlackyPaw and geNAZt
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;

import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketSetScore extends Packet implements PacketClientbound {

    public static final int TYPE_CHANGE = 0;
    public static final int TYPE_REMOVE = 1;

    private byte type;
    private List<ScoreEntry> entries;

    /**
     * Construct a new packet
     */
    public PacketSetScore() {
        super(Protocol.PACKET_SET_SCORE);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeByte(this.type);
        buffer.writeUnsignedVarInt(this.entries.size());

        for (ScoreEntry entry : this.entries) {
            buffer.writeSignedVarLong(entry.scoreId);
            buffer.writeString(entry.objective);
            buffer.writeLInt(entry.score);

            if (this.type == TYPE_CHANGE) {
                buffer.writeByte(entry.entityType);
                switch (entry.entityType) {
                    case ScoreEntry.TYPE_FAKE_PLAYER: // Fake entity
                        buffer.writeString(entry.fakeEntity);
                        break;
                    case ScoreEntry.TYPE_PLAYER:
                    case ScoreEntry.TYPE_ENTITY:
                        buffer.writeUnsignedVarLong(entry.entityId);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown entity type " + entry.entityType + " for score entry");
                }
            }
        }
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {

    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public List<ScoreEntry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<ScoreEntry> entries) {
        this.entries = entries;
    }

    public static class ScoreEntry {

        public static final int TYPE_PLAYER = 1;
        public static final int TYPE_ENTITY = 2;
        public static final int TYPE_FAKE_PLAYER = 3;

        private final long scoreId;
        private final String objective;
        private final int score;

        // Add entity type
        private byte entityType;
        private String fakeEntity;
        private long entityId;

        public ScoreEntry(long scoreId, String objective, int score) {
            this.scoreId = scoreId;
            this.objective = objective;
            this.score = score;
        }

        public ScoreEntry(long scoreId, String objective, int score, byte entityType, String fakeEntity, long entityId) {
            this.scoreId = scoreId;
            this.objective = objective;
            this.score = score;
            this.entityType = entityType;
            this.fakeEntity = fakeEntity;
            this.entityId = entityId;
        }

        public long getScoreId() {
            return this.scoreId;
        }

        public String getObjective() {
            return this.objective;
        }

        public int getScore() {
            return this.score;
        }

        public byte getEntityType() {
            return this.entityType;
        }

        public String getFakeEntity() {
            return this.fakeEntity;
        }

        public long getEntityId() {
            return this.entityId;
        }
    }

}
