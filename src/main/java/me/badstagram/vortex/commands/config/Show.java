package me.badstagram.vortex.commands.config;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class Show extends SubCommand {
    public Show() {
        this.name = "show";
        this.category = new Category("Config");

    }

    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {
        ctx.getChannel().sendMessage("Hello from Show.java").queue();

    }
}
