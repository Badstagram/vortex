package me.badstagram.vortex.commands.config.set;

import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class InviteAction extends SubCommand {
    private final String value;

    public InviteAction(String value) {
        this.value = value;
    }

    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {

    }
}
