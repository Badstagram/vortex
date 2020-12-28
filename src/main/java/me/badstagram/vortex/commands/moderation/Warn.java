package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.entities.GuildPunishmentType;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GuildPunishmentManager;
import me.badstagram.vortex.util.ArgumentParser;
import net.dv8tion.jda.api.Permission;

public class Warn extends Command {
    public Warn() {
        this.name = "warn";
        this.help = "Warn a member";
        this.usage = "warn <member> <reason>";
        this.userPermissions = new Permission[] {Permission.NICKNAME_MANAGE};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        if (ctx.getArgs().isEmpty()) throw new BadArgumentException("member", true);

        var parser = new ArgumentParser(ctx.getArgs(), ctx.getMessage(), ctx.getJDA(), ctx.getGuild());

        var parsedMembers = parser.parseMember();

        if (parsedMembers.isEmpty()) throw new BadArgumentException("member", false);


        try {
            var target = parsedMembers.get(0);
            var targetUser = target.getUser();

            var reason = String.join(" ", ctx.getArgs()
                    .subList(1, ctx.getArgs()
                            .size()));

            if (reason.isEmpty()) throw new BadArgumentException("reason", true);


            targetUser.openPrivateChannel()
                    .flatMap(ch -> ch.sendMessageFormat("You have been warned in %s by %s for %s", ctx.getGuild()
                            .getName(), ctx.getAuthor()
                            .getAsTag(), reason))
                    .queue();

            var caseId = new GuildPunishmentManager(targetUser.getId(), ctx.getGuild()
                    .getId())
                    .createCase(reason, GuildPunishmentType.WARN, ctx.getMember()
                            .getId(), false, null, null);

            ctx.getChannel()
                    .sendMessageFormat(":white_check_mark: %#s has been warned for `%s` | Case #%d", targetUser, reason,
                            caseId)
                    .queue();


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
