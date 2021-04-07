package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.duncte123.loadingbar.LoadingBar;
import net.dv8tion.jda.api.Permission;

import java.util.Calendar;

public class ProgressBar extends Command {
    public ProgressBar() {
        this.name = "progress";
        this.help = "Displays a progress bar that shows how much of the year has passed";
        this.botPermissions = new Permission[] {Permission.MESSAGE_ATTACH_FILES};
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        try {
            var percent = LoadingBar.getPercentage();

            var args = ctx.getArgs();
            var year = args.isEmpty() ? Calendar.getInstance().get(Calendar.YEAR) : Integer.parseInt(args.get(0));

            ctx.getChannel()
                    .sendMessageFormat("**%s** is **%s**%% complete.", year, Math.round(percent))
                    .addFile(LoadingBar.generateImage(percent), "bar.png")
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
