package me.badstagram.vortex.commands.filter;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.DatabaseUtils;

public class FilterAdd extends SubCommand {
    public FilterAdd() {
        this.category = new Category("Filter");
    }

    @Override
    public void execute(SubCommandContext ctx) throws BadArgumentException, CommandExecutionException {
        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("word", true);

        var word = args.get(0);

        try {
            DatabaseUtils.execute("UPDATE guild_config SET word_filter = concat(word_filter, ?, ';')", word);

            ctx.getChannel()
                    .sendMessageFormat("`%s` added to the word filter.", word)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
