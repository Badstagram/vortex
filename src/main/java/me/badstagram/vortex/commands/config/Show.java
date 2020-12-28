package me.badstagram.vortex.commands.config;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.commandhandler.context.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class Show extends SubCommand {
    public Show() {
        this.name = "show";
    }

    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {
        ctx.getChannel().sendMessage("Hello from Show.java").queue();

    }
}
