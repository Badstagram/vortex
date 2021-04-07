package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;

import java.net.URL;

public class Jumbo extends Command {
    public Jumbo() {
        this.name = "jumbo";
        this.help = "Make an emote YUGE <:angrytrump:829076264593129512>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_ATTACH_FILES};
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var emotes = ctx.getMessage()
                .getEmotes();

        if (emotes.isEmpty()) {
            throw new BadArgumentException("emote");
        }

        try {
            var emote = emotes.get(0);

            ctx.getChannel()
                    .sendFile(new URL(emote.getImageUrl()).openStream(), "%s.%s".formatted(emote.getName(), emote.isAnimated() ? "png" : "gif"))
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
