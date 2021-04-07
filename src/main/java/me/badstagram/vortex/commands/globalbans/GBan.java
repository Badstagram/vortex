package me.badstagram.vortex.commands.globalbans;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class GBan extends Command {

    public GBan() {
        this.name = "gban";
        this.help = "";
        this.usage = "-gban <report|forceadd|approve|deny> <user_id> <proof> <reason>";
        this.category = new Category("Global Ban");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty())
            throw new BadArgumentException("action", true);

        switch (args.get(0)) {
            case "report" -> new GBanReport().execute(new SubCommandContext(ctx));
            case "approve" -> new GBanApprove().execute(new SubCommandContext(ctx));
            case "deny" -> new GBanDeny().execute(new SubCommandContext(ctx));
            case "forceadd" -> new GBanForceAdd().execute(new SubCommandContext(ctx));

            default -> throw new BadArgumentException("action", false);
        }


    }
}
