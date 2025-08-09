package io.gomint.server.network.packet.types;

import io.gomint.jraknet.PacketBuffer;

import java.util.HashMap;
import java.util.Map;

public class Experiments {

    private Map<String, Boolean> experiments;

    private boolean hasPreviouslyUsedExperiments;

    public Experiments(Map<String, Boolean> experiments, boolean hasPreviouslyUsedExperiments) {
        this.experiments = experiments;
        this.hasPreviouslyUsedExperiments = hasPreviouslyUsedExperiments;
    }

    public void write(PacketBuffer buffer) {
        buffer.writeLInt(this.experiments.size());
        for (Map.Entry<String, Boolean> entry : this.experiments.entrySet()) {
            buffer.writeString(entry.getKey());
            buffer.writeBoolean(entry.getValue());
        }
        buffer.writeBoolean(this.hasPreviouslyUsedExperiments);
    }

    public static Experiments read(PacketBuffer buffer) {
        int size = buffer.readLInt();
        Map<String, Boolean> experiments = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = buffer.readString();
            boolean value = buffer.readBoolean();
            experiments.put(key, value);
        }
        boolean hasPreviouslyUsedExperiments = buffer.readBoolean();
        return new Experiments(experiments, hasPreviouslyUsedExperiments);
    }

    public Map<String, Boolean> getExperiments() {
        return this.experiments;
    }

    public void setExperiments(Map<String, Boolean> experiments) {
        this.experiments = experiments;
    }
}
