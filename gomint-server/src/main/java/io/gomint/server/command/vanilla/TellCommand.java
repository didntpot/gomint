package io.gomint.server.command.vanilla;

import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;
import io.gomint.command.annotation.*;
import io.gomint.command.validator.TargetValidator;
import io.gomint.command.validator.TextValidator;
import io.gomint.server.entity.EntityPlayer;

import java.util.Map;

/**
 * @author DelxHQ
 * @version 1.0
 */

@Name("tell")
@Alias("w")
@Description("Sends a private message to one or more players.")
@Permission("gomint.command.tell")
@Overload({
    @Parameter(name = "player", validator = TargetValidator.class),
    @Parameter(name = "message", validator = TextValidator.class)
})
public class TellCommand extends Command {
    @Override
    public CommandOutput execute(CommandSender<?> commandSender, String alias, Map<String, Object> arguments) {
        EntityPlayer target = (EntityPlayer) arguments.get("player");
        EntityPlayer sender = (EntityPlayer) commandSender;

        String message = (String) arguments.get("message");
        if (target == null) {
            return CommandOutput.failure("You must provide a player!");
        }

        if (message == null) {
            return CommandOutput.failure("You must provide a message!");
        }

        target.sendMessage(String.format("<%s> %s whispers to you: %s", sender.name(), sender.name(), message));
        return CommandOutput.successful("You whisper to %%s: %%s", target.name(), message);
    }
}
