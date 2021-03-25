package me.badstagram.vortex.commands.globalbans;

import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.EmbedUtil;

import java.time.Instant;
import java.util.UUID;

class GBanForceAdd extends SubCommand {

    // user_id proof reason
    @Override
    public void execute(SubCommandContext ctx) throws BadArgumentException, CommandExecutionException {
        if (!ctx.getAuthor().getId().equals(ctx.getClient().getOwnerId())) {
            ctx.getChannel().sendMessage(
                    ":x: You don't have the right permissions for this command. You need Bot Owner").queue();
            return;
        }

        var args = ctx.getArgs();

        var userId = args.get(0);
        var proof = args.get(1);
        var reason = String.join(" ", args.subList(2, args.size()));

        ctx.getJDA()
                .retrieveUserById(userId)
                .submit()
                .thenAccept(user -> {
                    var embed = EmbedUtil.createDefault()
                            .setTitle("New global added")
                            .addField("Moderator", "%#s (%s)".formatted(ctx.getAuthor(), ctx.getAuthor()
                                    .getId()), false)
                            .addField("User", "%#s (%s)".formatted(user, user.getId()), false)
                            .addField("Reason", reason, false)
                            .addField("Proof", proof, false)
                            .build();

                    ctx.getChannel().sendMessage(embed).queue();

                    ctx.getJDA()
                            .getGuildById(705938001519181877L)
                            .getTextChannelById(782298235830140998L)
                            .sendMessage(embed)
                            .queue();

                });

        try {
            DatabaseUtils.execute("INSERT INTO global_bans (user_id, moderator_id, proof, status, approved_by, ban_id, time_stamp, reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", userId,
                    ctx.getAuthor()
                            .getId(),
                    proof, GlobalBanStatus.APPROVED.getName(), UUID.randomUUID().toString(), Instant.now()
                            .toEpochMilli(), reason);
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }


    }
}
