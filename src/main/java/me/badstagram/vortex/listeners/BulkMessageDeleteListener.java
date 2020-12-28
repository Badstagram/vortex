package me.badstagram.vortex.listeners;

import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.ErrorHandler;
import me.badstagram.vortex.util.MiscUtil;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class BulkMessageDeleteListener extends ListenerAdapter {

    @Override
    public void onMessageBulkDelete(@NotNull MessageBulkDeleteEvent event) {
        var settingsManager = new GuildSettingsManager(event.getGuild().getId());
        var modLogChannelId = settingsManager.getModLogChannel();

        var modLog = event.getGuild()
                .getTextChannelById(modLogChannelId);

        if (modLog == null) return;

        var cache = Vortex.getCache();

        var messages = cache.getMessages(event.getGuild(), msg -> event.getMessageIds().contains(msg.getId()));

        var format = """
        %s - @%s - %s - #%s
        """; // timestamp - @userTag - content - #channel
        var sb = new StringBuilder("timestamp - @username - content - #channel\n");

        for (var msg : messages) {
            sb.append(format.formatted(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm").format(msg.getTimeCreated()),
                    msg.getAuthor(event.getJDA()).getAsTag(), msg.getContentRaw(),
                    msg.getTextChannel(event.getGuild()).getName()));
        }

        try {
            var embed = EmbedUtil.createDefaultError()
                    .setTitle("Channel Purged")
                    .addField("Channel", event.getChannel().getAsMention(), false)
                    .addField("Messages", MarkdownUtil.maskedLink("Here", MiscUtil.postToHasteBin(sb.toString())),
                            false)
                    .addField("Messages Deleted", String.valueOf(messages.size()), false)
                    .build();

            modLog.sendMessage(embed).queue();


        } catch (IOException e) {
            ErrorHandler.handle(e);
        }

    }
}
