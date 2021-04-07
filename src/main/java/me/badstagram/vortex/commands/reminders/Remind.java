package me.badstagram.vortex.commands.reminders;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import me.badstagram.vortex.util.MiscUtil;
import net.dv8tion.jda.api.Permission;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

public class Remind extends Command {
    public Remind() {
        this.name = "remind";
        this.help = "Create a reminder";
        this.usage = "remind <time> <reminder>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("reminders");


    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.size() < 2)
            throw new BadArgumentException("time");

        try {
            var timeString = args.get(0);
            var reminder = String.join(" ", args.subList(1, args.size()));

            var period = MiscUtil.parseTimeString(timeString);

            var time = FormatUtil.secondsToTime(period.toStandardDuration()
                    .getStandardSeconds());


            var now = OffsetDateTime.now();
            var then = now.plus(period.toStandardDuration()
                    .getMillis(), ChronoUnit.MILLIS);

            var result = DatabaseUtils.executeQuery("INSERT INTO reminders (user_id, reminder, channel_id, guild_id, remind_at, jump_url) VALUES (?, ?, ?, ?, ?, ?) RETURNING reminder_id",
                    ctx.getAuthor()
                            .getId(),
                    reminder,
                    ctx.getChannel()
                            .getId(),
                    ctx.getGuild()
                            .getId(),
                    then.toInstant()
                            .toEpochMilli(),
                    ctx.getMessage()
                            .getJumpUrl());

            var reminderId = (int) result.get("reminder_id");

            var embed = EmbedUtil.createDefault()
                    .setTitle("Reminder")
                    .setDescription("I'll remind you in %s %s".formatted(time, reminder))
                    .setFooter("Reminder ID " + reminderId)
                    .build();


            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();


            Vortex.getScheduler()
                    .createScheduledTask(period.toStandardDuration(), () -> ctx.getChannel()
                            .sendMessageFormat("""
                                    \u23F0 Hey %s,
                                    %s ago you wanted me to remind you %s
                                                                        
                                    Original Message: %s
                                    """, ctx.getAuthor()
                                    .getAsMention(), time, reminder, ctx.getMessage()
                                    .getJumpUrl())
                            .queue());
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
