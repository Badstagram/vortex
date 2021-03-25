package me.badstagram.vortex.commands.economy;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class AddMoney extends Command {
    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("member");

        var parser = ctx.createArgumentParser();


    }
}
