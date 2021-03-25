package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ReverseAvatar extends Command {
    public ReverseAvatar() {
        this.name = "reverseavatar";
        this.aliases = new String[]{"revav"};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var users = ctx.createArgumentParser()
                .parseUser();

        var user = users.isEmpty() ? ctx.getAuthor() : users.get(0);

        var embed = EmbedUtil.createDefault()
                .setDescription("[`Search %s's Avatar`](https://images.google.com/searchbyimage?image_url=%s)".formatted(user.getName(), URLEncoder.encode(user.getEffectiveAvatarUrl(), StandardCharsets.UTF_8)))
                .build();


        ctx.getChannel()
                .sendMessage(embed)
                .queue();

    }
}
