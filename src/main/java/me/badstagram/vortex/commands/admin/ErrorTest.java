package me.badstagram.vortex.commands.admin;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.User;

public class ErrorTest extends Command {
    public ErrorTest() {
        this.name = "errortest";
        this.help = "Test the error handler";
        this.owner = true;
        this.usage = "-errorhandler";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            User user = null;

            ctx.reply(user.getAsTag());
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
