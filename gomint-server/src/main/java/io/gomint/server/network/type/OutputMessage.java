package io.gomint.server.network.type;

import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
public class OutputMessage {

    private String messageId;
    private boolean internal;
    private List<String> parameters;

    public OutputMessage(String messageId, boolean success, List<String> parameters) {
        this.messageId = messageId;
        this.internal = success;
        this.parameters = parameters;
    }

    public String messageId() {
        return this.messageId;
    }

    public boolean internal() {
        return this.internal;
    }

    public List<String> parameters() {
        return this.parameters;
    }
}
