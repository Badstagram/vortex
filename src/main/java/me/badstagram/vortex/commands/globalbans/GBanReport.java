package me.badstagram.vortex.commands.globalbans;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GlobalBanManager;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;

import java.util.EnumSet;

class GBanReport extends SubCommand {
    public GBanReport() {
        this.category = new Category("Global Ban");

    }

    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs().subList(1, ctx.getArgs().size());

        if (args.isEmpty()) {
            throw new BadArgumentException("user_id", true);
        }

        if (args.size() == 2) {
            throw new BadArgumentException("proof", true);
        }

        if (args.size() == 1) {
            throw new BadArgumentException("reason", true);
        }

        try {

            var userId = args.get(0);
            var proof = args.get(1);
            var reason = String.join(" ", args.subList(2, args.size()));

            var banId = GlobalBanManager.createBanReport(userId, ctx.getMember().getId(), reason, proof);

            ctx.getJDA()
                    .retrieveUserById(userId)
                    .submit()
                    .whenCompleteAsync((user, err) -> {

                        if (err != null) {
                            return;
                        }

                        var embed = EmbedUtil.createDefault()
                                .setTitle("New global ban report received")
                                .addField("Moderator", "%#s (%s)".formatted(ctx.getAuthor(), ctx.getAuthor().getId()),
                                        false)
                                .addField("User", "%#s (%s)".formatted(user, user.getId()), false)
                                .addField("Reason", reason, false)
                                .addField("Proof", proof, false)
                                .setFooter("Report ID: %s".formatted(banId))
                                .build();

                        ctx.getJDA()
                                .getGuildById(705938001519181877L)
                                .getTextChannelById(782298235830140998L)
                                .sendMessage("<@&782300170800857128>")
                                .allowedMentions(EnumSet.of(Message.MentionType.ROLE))
                                .embed(embed)
                                .queue();
                    });

            var content = """
                    Hey %s!
                    This is an automated message letting you know that we have received your Global Ban Report!
                    You will recieve a follow up message once our team has handled it!
                    The report ID is `%s`
                                        
                    We thank you for keeping Discord a safe and friendly place!
                    """;

            ctx.getAuthor()
                    .openPrivateChannel()
                    .flatMap(ch -> ch.sendMessageFormat(content, ctx.getAuthor().getName(), banId))
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
