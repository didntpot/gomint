package io.gomint.server.network.packet.types;

import io.gomint.taglib.NBTTagCompound;

public class ItemComponentPacketEntry {

    private String name;
    private NBTTagCompound componentNbt;

    public String getName() {
        return name;
    }

    public NBTTagCompound getComponentNbt() {
        return componentNbt;
    }
}
