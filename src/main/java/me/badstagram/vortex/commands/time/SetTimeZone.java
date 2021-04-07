package me.badstagram.vortex.commands.time;

import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.EmbedUtil;

public class SetTimeZone extends SubCommand {


    @Override
    public void execute(SubCommandContext ctx) throws BadArgumentException, CommandExecutionException {
        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("timezone", true);

        var timezone = args.get(0);


        try {
            DatabaseUtils.execute("UPDATE user_config SET time_zone = ? WHERE user_id = ?", timezone, ctx.getAuthor()
                    .getId());

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

        var embed = EmbedUtil.createDefault()
                .setTitle("Success")
                .setDescription("Your timezone has been set to %s. Use -timefor to see the current time.".formatted(timezone))
                .build();

        ctx.getChannel()
                .sendMessage(embed)
                .queue();


    }
}
