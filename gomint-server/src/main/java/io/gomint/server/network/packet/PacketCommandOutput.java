package io.gomint.server.network.packet;

import io.gomint.jraknet.PacketBuffer;
import io.gomint.server.network.Protocol;
import io.gomint.server.network.type.CommandOrigin;
import io.gomint.server.network.type.OutputMessage;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketCommandOutput extends Packet implements PacketClientbound {

    public static final byte TYPE_LAST = 1;
    public static final byte TYPE_SILENT = 2;
    public static final byte TYPE_ALL = 3;
    public static final byte TYPE_DATA_SET = 4;

    private CommandOrigin origin;
    private byte outputType;
    private int successCount;
    private List<OutputMessage> outputs;
    private String unknownString;

    /**
     * Construct a new packet
     */
    public PacketCommandOutput() {
        super(Protocol.PACKET_COMMAND_OUTPUT);
    }

    @Override
    public void serialize(PacketBuffer buffer, int protocolID) {
        writeCommandOrigin(this.origin, buffer);
        buffer.writeByte(this.outputType);
        buffer.writeUnsignedVarInt(this.successCount);

        buffer.writeUnsignedVarInt(this.outputs.size());
        for (OutputMessage message : this.outputs) {
            buffer.writeBoolean(message.internal());
            buffer.writeString(message.messageId());
            buffer.writeUnsignedVarInt(message.parameters().size());
            for (String parameter : message.parameters()) {
                buffer.writeString(parameter);
            }
        }
    }

    @Override
    public void deserialize(PacketBuffer buffer, int protocolID) {

    }

    public CommandOrigin getOrigin() {
        return this.origin;
    }

    public void setOrigin(CommandOrigin origin) {
        this.origin = origin;
    }

    public boolean isSuccess() {
        return this.successCount > 0;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public List<OutputMessage> getOutputs() {
        return this.outputs;
    }

    public void setOutputs(List<OutputMessage> outputs) {
        this.outputs = outputs;
    }
}
