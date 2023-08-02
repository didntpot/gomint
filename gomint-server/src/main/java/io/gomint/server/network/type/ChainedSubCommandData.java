package io.gomint.server.network.type;

import java.util.List;

public class ChainedSubCommandData {

    private String name;
    private List<ChainedSubCommandValue> values;

    public ChainedSubCommandData(String name, List<ChainedSubCommandValue> values) {
        this.name = name;
        this.values = values;
    }

    public String name() {
        return this.name;
    }

    public List<ChainedSubCommandValue> values() {
        return this.values;
    }

    public static class ChainedSubCommandValue {

        private String name;
        private int type;

        public ChainedSubCommandValue(String name, int type) {
            this.name = name;
            this.type = type;
        }

        public String name() {
            return this.name;
        }

        public int type() {
            return this.type;
        }
    }
}
