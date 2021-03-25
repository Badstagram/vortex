package me.badstagram.vortex.commands.admin;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class Sudo extends Command {
    public Sudo() {
        this.name = "sudo";
        this.help = "Run a command as another member";
        this.aliases = new String[] {"su"};
        this.owner = true;
        this.usage = "<member> <command> [args]";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();
    }


}
