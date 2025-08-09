package io.gomint.server.network.handler;

import io.gomint.command.CommandOutput;
import io.gomint.command.CommandOutputMessage;
import io.gomint.server.network.PlayerConnection;
import io.gomint.server.network.packet.PacketCommandOutput;
import io.gomint.server.network.packet.PacketCommandRequest;
import io.gomint.server.network.type.OutputMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author geNAZt
 * @version 1.0
 */
public class PacketCommandRequestHandler implements PacketHandler<PacketCommandRequest> {

    @Override
    public void handle(PacketCommandRequest packet, long currentTimeMillis, PlayerConnection connection) {
        // Sanity stuff
        if (!packet.getInputCommand().startsWith("/")) {
            return;
        }

        CommandOutput output = connection.entity().dispatchCommand(packet.getInputCommand());
        if (output != null) {
            PacketCommandOutput commandOutput = new PacketCommandOutput();
            commandOutput.setOutputType((byte) 3);
            commandOutput.setSuccessCount(output.success() ? 1 : 0);
            commandOutput.setOrigin(packet.getCommandOrigin().type((byte) 3));

            // Remap outputs
            List<OutputMessage> outputMessages = new ArrayList<>();
            for (CommandOutputMessage outputMessage : output.messages()) {
                outputMessages.add(new OutputMessage(outputMessage.format(), outputMessage.success(), outputMessage.parameters()));
            }

            commandOutput.setOutputs(outputMessages);

            connection.addToSendQueue(commandOutput);
        }
    }
}
