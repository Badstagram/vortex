package me.badstagram.vortex.commands.config;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.commandhandler.context.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class ConfigCommand extends Command {
    public ConfigCommand() {
        this.name = "config";
        this.help = "Show or set the guild configuration";
        this.usage = "config <set|show> <option> [value]";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.userPermissions = new Permission[] {Permission.MANAGE_SERVER};
        this.subCommands = new Command[] {new Set(), new Show()};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var validOpts = new String[] {"set", "show"};

        if (ctx.getArgs().isEmpty() || Arrays.stream(validOpts).noneMatch(opt -> opt.equalsIgnoreCase(ctx.getArgs()
                .get(0)))) {
            throw new BadArgumentException("set/show", true);
        }

        switch (ctx.getArgs().get(0).toLowerCase()) {
            case "set" -> new Set().execute(new SubCommandContext(ctx));
            case "show" -> new Show().execute(new SubCommandContext(ctx));

            default -> throw new BadArgumentException("action", false);
        }
    }
}
