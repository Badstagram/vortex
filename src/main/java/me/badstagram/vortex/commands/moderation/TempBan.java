package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.entities.enums.GuildPunishmentType;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GuildPunishmentManager;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import me.badstagram.vortex.util.MiscUtil;
import net.dv8tion.jda.api.Permission;
import org.joda.time.Duration;

public class TempBan extends Command {
    public TempBan() {
        this.name = "tempban";
        this.help = "Temporally bans a member from the guild";
        this.usage = "tempban <member> <time> <reason>";
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        var args = ctx.getArgs();

        if (args.size() < 3)
            throw new BadArgumentException("member");


        var members = ctx.createArgumentParser()
                .parseMember();

        if (members.isEmpty())
            throw new BadArgumentException("member", false);

        var member = members.get(0);

        var time = MiscUtil.parseTimeString(args.get(1));

        var reason = String.join(" ", args.subList(2, args.size()));

        var seconds = time.toStandardDuration()
                .getStandardSeconds();

        ctx.getChannel()
                .sendMessageFormat(":white_check_mark: %s has been banned for %s for %s", member.getUser().getAsTag(), FormatUtil.secondsToTimeCompact(seconds), reason)
                .queue();

        Duration duration;

        try {
            duration = time.toStandardDuration();
        } catch (UnsupportedOperationException e) {
            ctx.getChannel().sendMessage("Use `days` to ban for months or years.").queue();
            return;
        }

        var id = member.getId();
        var punishmentManager = new GuildPunishmentManager(member);
        var settingsManager = new GuildSettingsManager(ctx);

//        var caseId = punishmentManager.createCase(reason, GuildPunishmentType.TEMPBAN, ctx.getAuthor(), false. )

        var embed = EmbedUtil.createDefaultPunishment(GuildPunishmentType.TEMPBAN)
                .addField("User", member.getUser().getAsTag(), true)
                .addField("Responsible Moderator", ctx.getAuthor().getAsTag(), true)
                .addField("Reason", reason, true);





        Vortex.getScheduler().createScheduledTask(duration, () -> ctx.getGuild().unban(id).queue());
    }
}
