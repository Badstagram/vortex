package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.FormatUtil;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;

public class HowLong extends Command {
    public HowLong() {
        this.name = "howlong";
        this.usage = "howlong <date>";
        this.help = "Get the amount of time until a date. Use dd/mm/yyyy";
        this.category = new Category("Info");


    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("date", true);

        try {

            var now = new DateTime();
            var then = DateTime.parse(args.get(0), DateTimeFormat.forPattern("dd/MM/yyyy"));


            var seconds = Seconds.secondsBetween(now, then).getSeconds();

            ctx.getChannel()
                    .sendMessageFormat("There are %s until %s", FormatUtil.secondsToTime(seconds), args.get(0))
                    .queue();


        } catch (IllegalArgumentException e) {
            throw new BadArgumentException("date", false);
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
