package io.gomint.server.network.type;

import java.util.List;

public class CommandOverload {

    private boolean chaining;

    private List<CommandData.Parameter> parameters;

    public CommandOverload(
        boolean chaining,
        List<CommandData.Parameter> parameters
    ) {
        this.chaining = chaining;
        this.parameters = parameters;
    }

    public boolean chaining() {
        return this.chaining;
    }

    public List<CommandData.Parameter> parameters() {
        return this.parameters;
    }
}
