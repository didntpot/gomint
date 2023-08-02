package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.type.CommandOrigin;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketCommandRequest extends Packet implements PacketServerbound {

    private String inputCommand;
    private CommandOrigin commandOrigin;
    private boolean internal;
    private int version;

    /**
     * Construct a new packet
     */
    public PacketCommandRequest() {
        super(Protocol.PACKET_COMMAND_REQUEST);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {

    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {
        this.inputCommand = buffer.readString();
        this.commandOrigin = readCommandOrigin(buffer);
        this.internal = buffer.readBoolean();
        this.version = buffer.readSignedVarInt();
    }

    public String getInputCommand() {
        return this.inputCommand;
    }

    public void setInputCommand(String inputCommand) {
        this.inputCommand = inputCommand;
    }

    public CommandOrigin getCommandOrigin() {
        return this.commandOrigin;
    }

    public void setCommandOrigin(CommandOrigin commandOrigin) {
        this.commandOrigin = commandOrigin;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
