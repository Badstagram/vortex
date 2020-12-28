package me.badstagram.vortex.commands.globalbans;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class GBan extends Command {

    public GBan() {
        this.name = "gban";
        this.help = "";
        this.usage = "-gban <report|forceadd|approve|deny> <user_id> <proof> <reason>";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty())
            throw new BadArgumentException("action", true);

        switch (args.get(0)) {
            case "report" -> new GBanReport().execute(ctx);
            case "approve" -> new GBanApprove().execute(ctx);
            case "deny" -> new GBanDeny().execute(ctx);
            case "forceadd" -> new GBanForceAdd();

            default -> throw new BadArgumentException("action", false);
        }


    }
}
