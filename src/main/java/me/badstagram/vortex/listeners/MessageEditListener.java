package me.badstagram.vortex.listeners;

import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.Checks;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

public class MessageEditListener extends ListenerAdapter {

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        var guild = event.getGuild();
        var settingsManager = new GuildSettingsManager(guild);

        var modLogId = settingsManager.getModLogChannel();

        if (modLogId == null)
            return;

        var modLog = guild.getTextChannelById(modLogId);

        if (modLog == null || Checks.canEmbedLinks(modLog, guild.getSelfMember()))
            return;


        var oldMessage = Vortex.getCache()
                .getMessageById(event.getGuild(), event.getMessageIdLong());

        var newMessage = event.getMessage();

        if (!newMessage.isEdited() || oldMessage == null)
            return;

        var oldContent = oldMessage.getContentRaw();
        var newContent = newMessage.getContentRaw();
        var author = "%#s (%s)".formatted(newMessage.getAuthor(), newMessage.getAuthor()
                .getId());

        var jump = newMessage.getJumpUrl();
        var sendTime = FormatUtil.formatDate(oldMessage.getTimeCreated());
        var editTime = FormatUtil.formatDate(newMessage.getTimeEdited());
        var channel = "%s (`%#s`)".formatted(newMessage.getTextChannel()
                .getAsMention(), newMessage.getChannel());

        var embed = EmbedUtil.createDefaultWarning()
                .setTitle("Message Edited")
                .addField("Author", author, true)
                .addField("Channel", channel, true)
                .addField("Old Content", oldContent, true)
                .addField("New Content", newContent, true)
                .addField("Sent At", sendTime, true)
                .addField("Edited At", editTime, true)
                .addField("Jump", MarkdownUtil.maskedLink("Here", jump), true)
                .build();

        modLog.sendMessage(embed)
                .queue();

    }
}
