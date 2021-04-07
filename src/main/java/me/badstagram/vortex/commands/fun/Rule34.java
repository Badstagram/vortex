package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Colors;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.explodingbush.ksoftapi.enums.ImageType;

public class Rule34 extends Command {
    public Rule34() {
        this.name = "rule34";
        this.aliases = new String[]{"r34"};
        this.nsfw = true;
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var ksoft = Vortex.getKSoftAPI();
            var image = ksoft.getRedditImage(ImageType.RANDOM_REDDIT)
                    .setSubreddit("rule34")
                    .execute();

            var embed = new EmbedBuilder()
                    .setTitle(image.getTitle(), image.getSourceUrl())
                    .setColor(Colors.SUCCESS.getAsColor())
                    .setImage(image.getImageUrl())
                    .setTimestamp(image.getCreationTime())
                    .setFooter("Posted in %s by %s | Provided by KSoft.Si".formatted(image.getSubreddit(), image.getAuthor()))
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
