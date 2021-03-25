package me.badstagram.vortex.commands.globalbans;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.commandhandler.context.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GlobalBanManager;
import me.badstagram.vortex.util.EmbedUtil;

import java.util.UUID;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

class GBanApprove extends SubCommand {
    public GBanApprove() {
        this.name = "approve";
        this.usage = "gban approve <report_id>";
    }

    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var args = ctx.getArgs().subList(1, ctx.getArgs().size());


            if (args.isEmpty()) {
                throw new BadArgumentException("report_id", true);
            }

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

            var reportId = args.get(0);

           var ban = GlobalBanManager.approveBanReport(UUID.fromString(reportId), ctx.getAuthor().getId());

           if (ban == null) {
               ctx.getChannel()
                       .sendMessage("Something broke, try again later. In the meantime, here's a cookie \uD83C\uDF6A")
                       .queue();
               return;
           }


            ctx.getJDA()
                    .retrieveUserById(ban.getUserId())
                    .submit()
                    .whenCompleteAsync((user, err) -> {

                        if (err != null) {
                            return;
                        }

                        var embed = EmbedUtil.createDefault()
                                .setTitle("New global added")
                                .addField("Moderator", "%#s (%s)".formatted(ctx.getAuthor(), ctx.getAuthor().getId()), false)
                                .addField("User", "%#s (%s)".formatted(user, user.getId()), false)
                                .addField("Reason", ban.getReason(), false)
                                .addField("Proof", ban.getProof(), false)
                                .build();

                        ctx.getJDA()
                                .getGuildById(705938001519181877L)
                                .getTextChannelById(782298235830140998L)
                                .sendMessage(embed)
                                .queue();

                    });

            var content = """
                    Hey %s!
                    This is an automated message letting you know that your recent Vortex Global Ban report with id `%s` has been approved by %s!
                                        
                    We thank you for keeping Discord a safe and friendly place!
                    """;

            ctx.getJDA()
                    .retrieveUserById(ban.getModeratorId())
                    .submit()
                    .whenCompleteAsync((user, err) -> {
                        if (err != null) {
                            return;
                        }
                        user.openPrivateChannel()
                                .flatMap(ch -> ch.sendMessageFormat(content, user.getName(), ban.getBanId(), ctx.getAuthor()))
                                .queue();
                    });


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }
}
