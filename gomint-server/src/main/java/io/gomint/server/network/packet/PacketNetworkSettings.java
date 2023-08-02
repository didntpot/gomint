/*
 * Copyright (c) 2020 Gomint team
 *
 * This code is licensed under the BSD license found in the
 * LICENSE file in the root directory of this source tree.
 */

package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;

public class PacketNetworkSettings extends Packet implements PacketClientbound {

    public static final short COMPRESS_NOTHING = 0;
    public static final short COMPRESS_EVERYTHING = 1;

    private short compressionThreshold;
    private short compressionAlgorithm;
    private boolean enableClientThrottling;
    private byte clientThrottleThreshold;
    private float clientThrottleScalar;

    public PacketNetworkSettings() {
        super(Protocol.PACKET_NETWORK_SETTINGS);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) throws Exception {
        buffer.writeLShort(this.compressionThreshold);
        buffer.writeLShort(this.compressionAlgorithm);
        buffer.writeBoolean(this.enableClientThrottling);
        buffer.writeByte(this.clientThrottleThreshold);
        buffer.writeLFloat(this.clientThrottleScalar);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {
        this.compressionThreshold = buffer.readLShort();
        this.compressionAlgorithm = buffer.readLShort();
        this.enableClientThrottling = buffer.readBoolean();
        this.clientThrottleThreshold = buffer.readByte();
        this.clientThrottleScalar = buffer.readLFloat();
    }

    public short getCompressionThreshold() {
        return this.compressionThreshold;
    }

    public void setCompressionThreshold(short compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    public short getCompressionAlgorithm() {
        return this.compressionAlgorithm;
    }

    public void setCompressionAlgorithm(short compressionAlgorithm) {
        this.compressionAlgorithm = compressionAlgorithm;
    }

    public boolean getEnableClientThrottling() {
        return this.enableClientThrottling;
    }

    public void setEnableClientThrottling(boolean enableClientThrottling) {
        this.enableClientThrottling = enableClientThrottling;
    }

    public byte getClientThrottleThreshold() {
        return this.clientThrottleThreshold;
    }

    public void setClientThrottleThreshold(byte clientThrottleThreshold) {
        this.clientThrottleThreshold = clientThrottleThreshold;
    }

    public float getClientThrottleScalar() {
        return this.clientThrottleScalar;
    }

    public void setClientThrottleScalar(float clientThrottleScalar) {
        this.clientThrottleScalar = clientThrottleScalar;
    }
}
