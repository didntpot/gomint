package io.gomint.server.network.packet.types;

public class ItemPaletteEntry {

    private String stringId;
    private short numericId;
    private boolean componentBased;

    public ItemPaletteEntry(String stringId, short numericId, boolean componentBased) {
        this.stringId = stringId;
        this.numericId = numericId;
        this.componentBased = componentBased;
    }

    public String getStringId() {
        return this.stringId;
    }

    public short getNumericId() {
        return this.numericId;
    }

    public boolean isComponentBased() {
        return this.componentBased;
    }
}
