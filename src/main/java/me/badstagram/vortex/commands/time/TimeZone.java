package me.badstagram.vortex.commands.time;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;

public class TimeZone extends Command {
    public TimeZone() {
        this.name = "timezone";
        this.help = "Set, show or clear your timezone";
        this.usage = "timezone [set|clear]";
        this.botPermissions = new Permission[] { Permission.MESSAGE_EMBED_LINKS };
        this.aliases = new String[] { "tz" };
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty()) {
            this.showTimezone(ctx);
            return;
        }

        switch (args.get(0).toLowerCase()) {
            case "set" -> new SetTimeZone().execute(new SubCommandContext(ctx));
//            case "clear" -> new ClearTimeZone(new SubCommandContext(ctx));
        }
    }

    protected void showTimezone(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

    }
}
