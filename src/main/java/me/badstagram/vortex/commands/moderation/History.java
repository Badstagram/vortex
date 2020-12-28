package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GuildPunishmentManager;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.ArgumentParser;
import net.dv8tion.jda.api.Permission;

import java.util.stream.Collectors;

public class History extends Command {
    public History() {
        this.name = "history";
        this.aliases = new String[] {"hist"};
        this.help = "Get the punishment history of yourself or another member";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {


        try {
            if (ctx.getArgs().isEmpty() && !ctx.getMember().hasPermission(Permission.NICKNAME_MANAGE)) {
                ctx.getChannel()
                        .sendMessage("You do not have permission to view the punishment history of other members")
                        .queue();
                return;
            }

            var parser = new ArgumentParser(ctx.getArgs(), ctx.getMessage(), ctx.getJDA(), ctx.getGuild());

            var parsed = parser.parseMember();

            var member = parsed.isEmpty() ? ctx.getMember() : parsed.get(0);

            var punishmentManager = new GuildPunishmentManager(member.getId(), ctx.getGuild().getId());

            var history = punishmentManager.getHistory();

            var cases = history.stream()
                    .map(punishment -> "**[%d]** %s - <@%s>"
                            .formatted(punishment.getCaseId(), punishment.getReason(), punishment.getModId()))
                    .collect(Collectors.joining("\n"));

            var embed = EmbedUtil.createDefault()
                    .setTitle(member.getUser().getAsTag())
                    .setDescription(cases.isEmpty() ? "No Punishments" : cases)
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }


    }
}
