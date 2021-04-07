package me.badstagram.vortex.listeners;

import me.badstagram.vortex.automod.AutoMod;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

        var author = event.getAuthor();
        var message = event.getMessage();
        var content = message.getContentRaw();


        if (author.isBot() || message.isWebhookMessage()) {
            return;
        }


        if ("\uD83C\uDFD3".equals(content)) {
            var client = Vortex.getCommandClient();
            var ctx = new CommandContext(event, "", client);
            var cmd = client.getCommand("ping");

            try {
                cmd.execute(ctx);
            } catch (CommandExecutionException | BadArgumentException | CantPunishException e) {
                ErrorHandler.handleCommandError(e, cmd, ctx);
            }
        }

        if ("open the pod bay doors".equalsIgnoreCase(content)) {
            message.getChannel()
                    .sendMessageFormat("I'm sorry %s i'm afraid I can't do that.", message.getAuthor()
                            .getName())
                    .queue();
        }

        var autoMod = new AutoMod(message);

        try {
            autoMod.wordFilter();
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }

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
                                .isEmpty() ? "Empty message (was probably a bot embed)" : msg
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

                        var attachments = msg.getAttachments();

                        if (!attachments.isEmpty()) {
                            var urls = attachments.stream()
                                    .map(Message.Attachment::getUrl)
                                    .collect(Collectors.joining(", "));

                            embed = new EmbedBuilder(embed)
                                    .addField("Attachments", urls, false)
                                    .build();
                        }

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

        if (content.equalsIgnoreCase("@someone")) {
            var members = message.getGuild()
                    .getMembers();

            var random = Vortex.getRandom();

            var member = members.get(random.nextInt(members.size()));

            message.getChannel()
                    .sendMessage(member.getAsMention())
                    .allowedMentions(EnumSet.of(Message.MentionType.USER))
                    .queue();
        }


        if (content.matches("(?i)(jason)( citron)?")) {
            try {
                message.getChannel()
                        .sendFile(new URL("https://discord.ceo/man.png").openStream(), "jason.jpg")
                        .queue();
            } catch (IOException e) {
                ErrorHandler.handle(e);
            }
        }
    }
}

