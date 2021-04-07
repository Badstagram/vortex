package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GuildPunishmentManager;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;

public class Case extends Command {
    public Case() {
        this.name = "case";
        this.help = "Get info about a punishment";
        this.usage = "case <case_id>";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("Moderation");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        if (ctx.getArgs().isEmpty()) {
            throw new BadArgumentException("case_id", true);
        }


        var idStr = ctx.getArgs().get(0);

        try {
            Integer.parseInt(idStr);
        } catch (NumberFormatException ignored) {
            throw new BadArgumentException("case_id", false);
        }

        try {
            var id = Integer.parseInt(idStr);

            var punishment = new GuildPunishmentManager(null, ctx.getGuild().getId())
                    .getCase(id);

            if (punishment == null) {
                ctx.getChannel()
                        .sendMessageFormat("No case was found with ID `%d`", id)
                        .queue();
                return;
            }

            var userId = punishment.getUserId();
            var modId = punishment.getModId();
            var reason = punishment.getReason();
            var type = punishment.getType();
            var length = punishment.getLength();

            var jda = ctx.getJDA();

            jda.retrieveUserById(userId)
                    .submit()
                    .thenAccept(user -> jda.retrieveUserById(modId)
                            .submit()
                            .thenAccept(mod -> {
                                var embed = EmbedUtil.createDefault()
                                        .setTitle("Case #%d".formatted(id))
                                        .appendDescription("**User:** %#s (%s)\n".formatted(user, user.getId()))
                                        .appendDescription("**Action:** %s\n".formatted(type.getName()))
                                        .appendDescription("**Reason:** %s\n".formatted(reason))
                                        .appendDescription("**Length:** %s\n".formatted(length))
                                        .appendDescription("**Moderator:** %#s (%s)\n".formatted(user, user.getId()))
                                        .build();

                                ctx.getChannel()
                                        .sendMessage(embed)
                                        .queue();
                            }));

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
