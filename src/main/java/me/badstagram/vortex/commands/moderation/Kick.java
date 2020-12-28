package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Constants;
import me.badstagram.vortex.entities.GuildPunishmentType;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GuildPunishmentManager;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.ArgumentParser;
import net.dv8tion.jda.api.Permission;

public class Kick extends Command {
    public Kick() {
        this.name = "kick";
        this.help = "Kick a member from the guild";
        this.botPermissions = new Permission[] {Permission.KICK_MEMBERS};
        this.userPermissions = new Permission[] {Permission.KICK_MEMBERS};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {


        if (ctx.getArgs().isEmpty()) throw new BadArgumentException("member", true);

        var parser = new ArgumentParser(ctx.getArgs(), ctx.getMessage(), ctx.getJDA(), ctx.getGuild());

        var parsed = parser.parseMember();

        if (parsed.isEmpty()) throw new BadArgumentException("member", false);

        try {

            var target = parsed.get(0);
            var reason = String.join(" ", ctx.getArgs()
                    .subList(1, ctx.getArgs()
                            .size()));

            if (target.getIdLong() == ctx.getAuthor()
                    .getIdLong()) {
                ctx.getChannel()
                        .sendMessage(":x: You can't kick yourself.")
                        .queue();
                return;
            }


            if (!ctx.getSelfMember()
                    .canInteract(target) || !ctx.getMember()
                    .canInteract(target)) {
                ctx.getChannel()
                        .sendMessageFormat(":x: %s could not be kicked.", target.getUser()
                                .getAsTag())
                        .queue();
                return;
            }


            target.getUser()
                    .openPrivateChannel()
                    .flatMap(ch -> ch.sendMessageFormat("You have been kicked from %s by %s for %s", ctx.getGuild()
                            .getName(), ctx.getAuthor()
                            .getAsTag(), reason))
                    .queue(null, (err) -> {
                    });


            ctx.getGuild()
                    .kick(target)
                    .queue(v -> {
                        try {
                            var caseId = new GuildPunishmentManager(target.getId(), ctx.getGuild()
                                    .getId())
                                    .createCase(reason, GuildPunishmentType.KICK, ctx.getMember()
                                            .getId(), false, null, null);

                            ctx.getChannel()
                                    .sendMessageFormat(":white_check_mark: %s has been kicked for `%s` | Case #%d",
                                            target.getUser()
                                                    .getAsTag(),
                                            reason, caseId)
                                    .queue();
                        } catch (Exception e) {
                            var embed = EmbedUtil.createDefaultError()

                                    .setTitle("There was an unexpected error while executing that command")
                                    .setDescription(
                                            "If this error persists, report it in the [Support Server](%s)".formatted(
                                                    Constants.SUPPORT_SERVER))
                                    .setFooter(
                                            "%s: %s".formatted(e.getCause()
                                                    .getClass()
                                                    .getCanonicalName(), e.getCause()
                                                    .getMessage()))
                                    .build();

                            ctx.getChannel()
                                    .sendMessage(embed)
                                    .queue();
                        }
                    }, ignored -> ctx.getChannel()
                            .sendMessageFormat(":x: %s could not be kicked.", target.getUser()
                                    .getAsTag())
                            .queue());
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }
}

