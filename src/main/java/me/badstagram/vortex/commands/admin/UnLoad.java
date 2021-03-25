package me.badstagram.vortex.commands.admin;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;

public class UnLoad extends Command {
    public UnLoad() {
        this.name = "unload";
        this.help = "Unload a command";
        this.usage = "unload <command>";
        this.owner = true;
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty())
            throw new BadArgumentException("command");

        var client = ctx.getClient();

        var cmd = client.getCommand(args.get(0));

        if (cmd == null) {
            throw new BadArgumentException("command", false);
        }

        client.removeCommand(cmd);

        var embed = EmbedUtil.createDefault()
                .setTitle("Command Unloaded")
                .setDescription("Command %s has been unloaded".formatted(cmd.getName()))
                .build();

        ctx.getChannel()
                .sendMessage(embed)
                .queue();


    }
}
