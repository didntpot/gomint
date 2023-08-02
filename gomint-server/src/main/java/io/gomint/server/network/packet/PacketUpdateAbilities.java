package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.packet.types.ability.AbilitiesData;

public class PacketUpdateAbilities extends Packet implements PacketClientbound {

    private AbilitiesData data;

    public PacketUpdateAbilities() {
        super(Protocol.PACKET_UPDATE_ABILITIES);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) throws Exception {
        this.data.encode(buffer);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) throws Exception {
        this.data = AbilitiesData.decode(buffer);
    }

    public AbilitiesData getData() {
        return data;
    }

    public void setData(AbilitiesData data) {
        this.data = data;
    }
}
