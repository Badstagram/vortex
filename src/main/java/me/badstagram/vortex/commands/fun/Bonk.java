package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;

public class Bonk extends Command {
    public Bonk() {
        this.name = "bonk";
        this.help = "Bonk another user";
        this.usage = "bonk <member>";
        this.botPermissions = new Permission[] { Permission.MESSAGE_EMBED_LINKS };
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var parsedMembers = ctx.createArgumentParser().parseUser();

        if (parsedMembers.isEmpty()) throw new BadArgumentException("user");

        var member = parsedMembers.get(0);

        var embed = EmbedUtil.createDefault()
                .setTitle("Bonk")
                .setImage("https://cdn.discordapp.com/emojis/759934836193361991.png?v=1")
                .setDescription("%s bonked %s".formatted(ctx.getAuthor().getAsMention(), member.getAsMention()))
                .build();

        ctx.getChannel()
                .sendMessage(embed)
                .queue();
    }
}
