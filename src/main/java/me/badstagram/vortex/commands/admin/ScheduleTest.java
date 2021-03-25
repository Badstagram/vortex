package me.badstagram.vortex.commands.admin;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class ScheduleTest extends Command {
    public ScheduleTest() {
        this.name = "scheduletest";
        this.help = "Test the scheduler";
        this.owner = true;
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        ctx.getChannel()
                .sendMessage("In 3 seconds I will ping you")
                .queue();

        Vortex.getScheduler()
                .createScheduledTask(3, ChronoUnit.SECONDS, () -> ctx.getChannel()
                        .sendMessage("<@424239181296959507>")
                        .queue());
    }
}
