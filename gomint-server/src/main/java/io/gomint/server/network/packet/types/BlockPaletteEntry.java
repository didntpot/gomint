package io.gomint.server.network.packet.types;

import io.gomint.taglib.NBTTagCompound;

public class BlockPaletteEntry {

    private String name;

    private NBTTagCompound states;

    public BlockPaletteEntry(String name, NBTTagCompound states) {
        this.name = name;
        this.states = states;
    }

    public String getName() {
        return this.name;
    }

    public NBTTagCompound getStates() {
        return this.states;
    }
}
