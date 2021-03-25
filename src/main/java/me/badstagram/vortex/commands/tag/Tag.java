package me.badstagram.vortex.commands.tag;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.TagManager;
import me.badstagram.vortex.util.EmbedUtil;

public class Tag extends Command {
    public Tag() {
        this.name = "tag";
        this.aliases = new String[]{"t"};
        this.subCommands = new Command[]{new TagCreate()};

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var tagManager = new TagManager(ctx.getGuild().getId());

        var tag = tagManager.getTag(String.join(" ", ctx.getArgs()));

        if (tag == null) {
            ctx.getChannel().sendMessage(":x: Unknown Tag.").queue();
            return;
        }

        var name = tag.getName();
        var value = tag.getValue();
        var author = tag.getAddedBy();

        ctx.getJDA()
                .retrieveUserById(author)
                .submit()
                .thenAccept(tagAuthor -> {

                    var embed = EmbedUtil.createDefault()
                            .setTitle(name)
                            .setDescription(value)
                            .setFooter("Submitted By: %s".formatted(tagAuthor.getAsTag()));
                });

    }
}
