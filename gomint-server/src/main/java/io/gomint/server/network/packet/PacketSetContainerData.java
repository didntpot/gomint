package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketSetContainerData extends Packet implements PacketClientbound {

    public static final int PROPERTY_FURNACE_SMELT_PROGRESS = 0;
    public static final int PROPERTY_FURNACE_REMAINING_FUEL_TIME = 1;
    public static final int PROPERTY_FURNACE_MAX_FUEL_TIME = 2;
    public static final int PROPERTY_FURNACE_STORED_XP = 3;
    public static final int PROPERTY_FURNACE_FUEL_AUX = 4;

    public static final int PROPERTY_BREWING_STAND_BREW_TIME = 0;
    public static final int PROPERTY_BREWING_STAND_FUEL_AMOUNT = 1;
    public static final int PROPERTY_BREWING_STAND_FUEL_TOTAL = 2;

    private byte windowId;
    private int key;
    private int value;

    public PacketSetContainerData() {
        super(Protocol.PACKET_SET_CONTAINER_DATA);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        buffer.writeByte(this.windowId);
        buffer.writeSignedVarInt(this.key);
        buffer.writeSignedVarInt(this.value);
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {

    }

    public byte getWindowId() {
        return this.windowId;
    }

    public void setWindowId(byte windowId) {
        this.windowId = windowId;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
