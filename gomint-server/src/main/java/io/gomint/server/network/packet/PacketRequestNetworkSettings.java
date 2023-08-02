package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;

public class PacketRequestNetworkSettings extends Packet implements PacketServerbound {

    private int protocolVersion;

    public PacketRequestNetworkSettings() {
        super(Protocol.PACKET_REQUEST_NETWORK_SETTINGS);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) throws Exception {
        buffer.writeInt(this.protocolVersion);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {
        this.protocolVersion = buffer.readInt();
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
