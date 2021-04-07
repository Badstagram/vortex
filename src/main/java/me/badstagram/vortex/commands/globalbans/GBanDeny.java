package me.badstagram.vortex.commands.globalbans;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GlobalBanManager;

import java.util.UUID;

class GBanDeny extends SubCommand {
    public GBanDeny() {
        this.name = "deny";
        this.help = "Deny a global ban report";
        this.category = new Category("Global Ban");


    }

    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs().subList(1, ctx.getArgs().size());


        if (args.isEmpty() || args.size() < 2) {
            throw new BadArgumentException("report_id", true);
        }

        try {
            if (ctx.getGuild().getIdLong() != 705938001519181877L) {
                ctx.getChannel().sendMessage("This command can only be used in the support server.").queue();
                return;
            }

            var hasRole = ctx.getMember()
                    .getRoles()
                    .stream()
                    .anyMatch(r -> r.getIdLong() == 782300170800857128L);

            if (!hasRole) {
                ctx.reply("You do not have permission to run this command");
                return;
            }


            var banId = UUID.fromString(args.get(0));
            var userId = GlobalBanManager.denyBanReport(banId);

            var reason = String.join(" ", args.subList(1, args.size()));

            var content = """
                    Hey %s!
                    This is an automated message letting you know that your recent Vortex Global Ban report with id `%s` has been denied by %s because `%s`!
                                        
                    We thank you for keeping Discord a safe and friendly place!
                    """;

            ctx.getJDA()
                    .openPrivateChannelById(userId)
                    .flatMap(ch -> ch.sendMessageFormat(content, banId.toString(), ctx.getAuthor().getAsTag(), reason))
                    .queue();


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
