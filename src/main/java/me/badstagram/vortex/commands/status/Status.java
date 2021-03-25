package me.badstagram.vortex.commands.status;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class Status extends Command {
    public Status() {
        this.name = "status";
        this.help = "Get the status of a service used by Vortex";
        this.usage = "status <discord|hosting|digitalocean>";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("service", true);

        switch (args.get(0).toLowerCase()) {
            case "discord" -> new DiscordStatus().execute(ctx);
            case "hosting", "digitalocean" -> new HostingStatus().execute(ctx);
            case "cloudflare" -> new CloudFlareStatus().execute(ctx);
            default -> throw new BadArgumentException("service", false);
        }
    }
}
