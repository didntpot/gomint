package io.gomint.server.command.vanilla;

import io.gomint.command.Command;
import io.gomint.command.CommandOutput;
import io.gomint.command.CommandSender;
import io.gomint.command.annotation.*;
import io.gomint.command.validator.TargetValidator;
import io.gomint.entity.EntityPlayer;
import io.gomint.event.entity.EntityDamageEvent;

import java.util.Map;

/**
 * @author Kaooot
 * @version 1.0
 */
@Name("kill")
@Description("Kills entities (players, mobs, etc.).")
@Permission("gomint.command.kill")
@Overload({
    @Parameter(name = "target", validator = TargetValidator.class, optional = true)
})
public class KillCommand extends Command {

    @Override
    public CommandOutput execute(CommandSender<?> commandSender, String alias, Map<String, Object> arguments) {
        EntityPlayer target = (EntityPlayer) arguments.getOrDefault("target", commandSender);

        if (target == null) {
            return CommandOutput.failure("No targets matched selector.");
        }

        target.attack(target.maxHealth(), EntityDamageEvent.DamageSource.COMMAND);

        return CommandOutput.successful("Killed " + target.name());
    }
}
