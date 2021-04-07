package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;

public class CheckBan extends Command {
    public CheckBan() {
        this.name = "checkban";
        this.help = "Check if a user is global banned on KSoft.Si.";
        this.usage = "checkban <user>";
        this.botPermissions = new Permission[] { Permission.MESSAGE_EMBED_LINKS };
        this.category = new Category("Info");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var ksoft = Vortex.getKSoftAPI();
            var parser = ctx.createArgumentParser();

            var parsedUsers = parser.parseUser();

            if (parsedUsers.isEmpty()) throw new BadArgumentException("user", true);

            var user = parsedUsers.get(0);

            var ban = ksoft.getBan()
                    .setUserId(user.getId())
                    .execute();

            if (!ban.exists()) {
                var embed = EmbedUtil.createDefault()
                        .setTitle("Not Banned")
                        .setDescription("%#s is not global banned".formatted(user))
                        .setFooter("This is a service provided by KSoft.Si")
                        .build();

                ctx.getChannel()
                        .sendMessage(embed)
                        .queue();

                return;
            }

            ctx.getJDA()
                    .retrieveUserById(ban.getModId())
                    .submit()
                    .thenAccept(mod -> {
                        var embed = EmbedUtil.createDefaultError()
                                .setTitle("Banned")
                                .setDescription("**%s** is global banned\n".formatted(user.getAsTag()))
                                .appendDescription("**Reason:** %s\n".formatted(ban.getReason()))
                                .appendDescription("**Moderator:** %s\n".formatted(mod.getAsTag()))
                                .appendDescription("**Proof:** Below")
                                .setImage(ban.getProof())
                                .setFooter("This is a service provided by KSoft.Si")
                                .build();
                        ctx.getChannel()
                                .sendMessage(embed)
                                .queue();
                    });
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }
}
