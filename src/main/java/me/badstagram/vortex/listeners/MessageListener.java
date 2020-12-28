package me.badstagram.vortex.listeners;

import me.badstagram.vortex.automod.AutoMod;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        var author = event.getAuthor();
        var message = event.getMessage();
        var content = message.getContentRaw();



        if (author.isBot() || message.isWebhookMessage()) {
            return;
        }

        if (content.equalsIgnoreCase("\uD83C\uDFD3")) {
            var client = Vortex.getCommandClient();
            var ctx = new CommandContext(event, "", client);
            var cmd = client.getCommand("ping");

            try {
                cmd.execute(ctx);
            } catch (CommandExecutionException | BadArgumentException e) {
                ErrorHandler.handleCommandError(e, cmd, ctx);
            }
        }

/*        var autoMod = new AutoMod(message);

        try {
            autoMod.wordFilter();
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }*/

        Vortex.getCache()
                .putMessage(message);

        var matcher = Message.JUMP_URL_PATTERN
                .matcher(content);

        if (matcher.find()) {
            var guildId = matcher.group("guild");
            var channelId = matcher.group("channel");
            var messageId = matcher.group("message");

            var guild = event.getJDA()
                    .getGuildById(guildId);

            if (guild == null) {
                return;
            }
            var channel = guild.getTextChannelById(channelId);

            if (channel == null) {
                return;
            }

            channel.retrieveMessageById(messageId)
                    .queue(msg -> {
                        var msgAuthor = msg.getAuthor();
                        var msgContent = msg.getContentRaw()
                                                 .isEmpty() ? "Empty message (was probably an attachment or bot embed)" : msg
                                                 .getContentRaw();

                        var embed = EmbedUtil.createDefault()
                                .setAuthor(msgAuthor.getAsTag(), null, msgAuthor.getEffectiveAvatarUrl())
                                .addField("Author", "%#s".formatted(author), false)
                                .addField("Guild", guild.getName(), false)
                                .addField("Channel", "%s (%s)".formatted(channel.getAsMention(), channel.getName()),
                                        false)
                                .addField("Content", msgContent, false)
                                .setTimestamp(msg.getTimeCreated())
                                .build();

                        message.getChannel()
                                .sendMessage(embed)
                                .queue();
                    });
        }

        if (content.matches("(?i)up up down down left right left right b( )?a( start)?")) {
            message.getChannel()
                    .sendMessage("[!] Granting Developer Privileges...")
                    .queue(msg -> msg.editMessage("[!] Developer Privileges Granted...")
                            .queueAfter(Vortex.getRandom()
                                    .nextInt(10), TimeUnit.SECONDS));
        }

    }
}
