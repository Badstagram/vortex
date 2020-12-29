package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.Permission;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BotInfo extends Command {
    public BotInfo() {
        this.name = "botinfo";
        this.help = "Get info about the bot";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var client = ctx.getClient();
            var ownerId = client.getOwnerId();

            ctx.getJDA()
                    .retrieveUserById(ownerId)
                    .submit()
                    .whenCompleteAsync((owner, err) -> {
                        if (err != null) {
                            ctx.getChannel()
                                    .sendMessageFormat("Something broke and the developer could not be retrieved. `%s`",
                                            err.getMessage().replaceAll("\\d{5}:", ""))
                                    .queue();
                            return;
                        }
                        var mostUsedCommandKey = client.getCommandUsages()
                                .entrySet()
                                .stream()
                                .filter(entry -> entry.getValue().equals(
                                        Collections.max(client.getCommandUsages().values())))
                                .map(Map.Entry::getKey)
                                .collect(Collectors.joining());

                        var mostUsedName = client.getCommand(mostUsedCommandKey).getName();

                        var embed = EmbedUtil.createDefault()
                                .setTitle("Bot Info")
                                .addField("Developer", owner.getAsTag(), true)
                                .addField("Library", "JDA", true)
                                .addField("Version", JDAInfo.VERSION, true)
                                .addField("Uptime", "null", false)
                                .addField("Guilds", String.valueOf(ctx.getJDA().getGuilds().size()), true)
                                .addField("Members", String.valueOf(ctx.getJDA().getUsers().size()), true)
                                .addField("Commands", String.valueOf(client.getCommands().size()), true)
                                .addField("Most Used", "%s - %d times".formatted(mostUsedName, client.getCommandUsages().get(mostUsedCommandKey)), true)
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
