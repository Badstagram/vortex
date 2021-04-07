package me.badstagram.vortex.commands.tag;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.TagManager;

public class TagCreate extends Command {

    public TagCreate() {
        this.name = "create";
    }

    @Override
    public void execute(CommandContext ctx) throws BadArgumentException, CommandExecutionException {
        var args = ctx.getArgs();

        if (args.isEmpty()) {
            throw new BadArgumentException("name", true);
        }

        var split = String.join(" ", args).split(" \\| ");

        var title = split[0];
        var value = split[1];

        var tagManager = new TagManager(ctx.getGuild().getId());

        tagManager.createTag(title, value, ctx.getAuthor().getId());

        ctx.getMessage().addReaction("\u2705").queue();


    }
}
