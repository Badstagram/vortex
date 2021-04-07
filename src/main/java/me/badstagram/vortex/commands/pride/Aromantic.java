package me.badstagram.vortex.commands.pride;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.MiscUtil;
import net.dv8tion.jda.api.Permission;

public class Aromantic extends Command {
    public Aromantic() {
        this.name = "aromantic";
        this.aliases = new String[] { "aro" };
        this.help = "Overlay the aromantic pride flag over a users avatar";
        this.usage = "aromantic [user]";
        this.botPermissions = new Permission[]{Permission.MESSAGE_ATTACH_FILES};
        this.category = new Category("pride");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        var users = ctx.createArgumentParser()
                .parseUser();

        var avatar = (users.isEmpty() ? ctx.getAuthor() : users.get(0)).getEffectiveAvatarUrl();

        ctx.getChannel()
                .sendTyping()
                .queue();

        var bytes = MiscUtil.getPrideFlag("aromantic", avatar);

        ctx.getChannel()
                .sendFile(bytes, "aromantic.%s".formatted(avatar.substring(avatar.length() - 3)))
                .queue();
    }
}
