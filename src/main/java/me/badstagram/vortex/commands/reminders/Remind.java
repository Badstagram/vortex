package me.badstagram.vortex.commands.reminders;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import me.badstagram.vortex.util.MiscUtil;
import net.dv8tion.jda.api.Permission;

public class Remind extends Command {
    public Remind() {
        this.name = "remind";
        this.help = "Create a reminder";
        this.usage = "remind <time> <reminder>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
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

            var embed = EmbedUtil.createDefault()
                    .setTitle("Reminder")
                    .setDescription("I'll remind you in %s %s".formatted(time, reminder))
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();


            Vortex.getScheduler()
                    .createScheduledTask(period.toStandardDuration(), () -> ctx.getChannel()
                            .sendMessageFormat("""
                                    Hey %s,
                                    You wanted me to remind you %s
                                    """, ctx.getAuthor()
                                    .getAsMention(), reminder)
                            .queue());
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
