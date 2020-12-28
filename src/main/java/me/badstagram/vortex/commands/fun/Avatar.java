package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.ArgumentParser;
import net.dv8tion.jda.api.Permission;

public class Avatar extends Command {
    public Avatar() {
        this.name = "avatar";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.usage = "avatar [user]";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var parser = new ArgumentParser(ctx.getArgs(), ctx.getMessage(), ctx.getJDA(), ctx.getGuild());

            var parsedUsers = parser.parseUser();

            var user = parsedUsers.isEmpty() ? ctx.getAuthor() : parsedUsers.get(0);

            var embed = EmbedUtil.createDefault()
                    .setImage(user.getEffectiveAvatarUrl())
                    .setAuthor(user.getAsTag())
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }
}
