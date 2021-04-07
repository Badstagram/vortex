package me.badstagram.vortex.commands.filter;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;

public class Filter extends Command {
    public Filter() {
        this.name = "filter";
        this.usage = "filter <add|remove> <word>";
        this.help = "Manage the word filter";
        this.userPermissions = new Permission[] { Permission.MANAGE_SERVER };
        this.category = new Category("Filter");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("action", true);

        try {
            switch (args.get(0).toLowerCase()) {
                case "add" -> new FilterAdd().execute(new SubCommandContext(ctx));
                case "remove" -> new FilterRemove().execute(new SubCommandContext(ctx));
                default -> throw new BadArgumentException("action", false);
            }
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
