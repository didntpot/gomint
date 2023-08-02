package io.gomint.server.network.packet.types;

import io.gomint.jraknet.PacketBuffer;

public class NetworkPermissions {

    private boolean disableClientSounds;

    public NetworkPermissions(boolean disableClientSounds) {
        this.disableClientSounds = disableClientSounds;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(this.disableClientSounds);
    }

    public static NetworkPermissions decode(PacketBuffer buffer) {
        boolean disableClientSounds = buffer.readBoolean();
        return new NetworkPermissions(disableClientSounds);
    }

    public boolean isDisableClientSounds() {
        return this.disableClientSounds;
    }

    public void setDisableClientSounds(boolean disableClientSounds) {
        this.disableClientSounds = disableClientSounds;
    }
}
