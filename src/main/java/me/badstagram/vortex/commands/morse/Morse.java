package me.badstagram.vortex.commands.morse;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class Morse extends Command {
    public Morse() {
        this.name = "morse";
        this.usage = "morse <encode|decode> <input>";
        this.subCommands = new Command[]{new Encode()};
        this.category = new Category("Moderation");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        if (ctx.getArgs().isEmpty())
            throw new BadArgumentException("action", true);

        switch (ctx.getArgs().get(0)) {
            case "encode" -> new Encode().execute(new SubCommandContext(ctx));
            case "decode" -> new Decode();
            default -> throw new BadArgumentException("action", false);
        }


    }
}
