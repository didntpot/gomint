package io.gomint.server.network.packet.types.entity;

import io.gomint.jraknet.PacketBuffer;

import java.util.HashMap;
import java.util.Map;

public class PropertySyncData {
    private Map<Integer, Integer> intProperties;
    private Map<Integer, Float> floatProperties;

    public PropertySyncData(Map<Integer, Integer> intProperties, Map<Integer, Float> floatProperties) {
        this.intProperties = intProperties;
        this.floatProperties = floatProperties;
    }

    public Map<Integer, Integer> getIntProperties() {
        return this.intProperties;
    }

    public Map<Integer, Float> getFloatProperties() {
        return this.floatProperties;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeUnsignedVarInt(this.intProperties.size());
        for (Map.Entry<Integer, Integer> entry : this.intProperties.entrySet()) {
            buffer.writeUnsignedVarInt(entry.getKey());
            buffer.writeSignedVarInt(entry.getValue());
        }
        buffer.writeUnsignedVarInt(this.floatProperties.size());
        for (Map.Entry<Integer, Float> entry : this.floatProperties.entrySet()) {
            buffer.writeUnsignedVarInt(entry.getKey());
            buffer.writeLFloat(entry.getValue());
        }
    }

    public static PropertySyncData read(PacketBuffer buffer) {
        int intPropertiesSize = buffer.readUnsignedVarInt();
        Map<Integer, Integer> intProperties = new HashMap<>(intPropertiesSize);
        for (int i = 0; i < intPropertiesSize; i++) {
            intProperties.put(buffer.readUnsignedVarInt(), buffer.readSignedVarInt());
        }
        int floatPropertiesSize = buffer.readUnsignedVarInt();
        Map<Integer, Float> floatProperties = new HashMap<>(floatPropertiesSize);
        for (int i = 0; i < floatPropertiesSize; i++) {
            floatProperties.put(buffer.readUnsignedVarInt(), buffer.readLFloat());
        }
        return new PropertySyncData(intProperties, floatProperties);
    }
}
