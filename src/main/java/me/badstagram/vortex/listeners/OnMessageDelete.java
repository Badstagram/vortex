package me.badstagram.vortex.listeners;

import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.Checks;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class OnMessageDelete extends ListenerAdapter {
    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {

        var guild = event.getGuild();

        var message = Vortex.getCache()
                .pullMessage(guild, event.getMessageIdLong());

        var settingsManager = new GuildSettingsManager(guild);

        var modLogId = settingsManager.getModLogChannel();

        if (Checks.noneNull(modLogId))
            return;


//        var modLog = guild.getTextChannelById(modLogId);
        var modLog = guild.getTextChannelById(754711037839802450L);

        if (Checks.noneNull(modLog) || !Checks.canEmbedLinks(modLog, guild.getSelfMember()))
            return;

        var embed = EmbedUtil.createDefaultError()
                .setTitle("Message Deleted")
                .addField("Author", "%s (%s)".formatted(message.getAsTag(), message.getAuthorId()), true)
                .addField("Channel", message.getTextChannel(guild)
                        .getAsMention(), true);

        var attachments = message.getAttachments();
        if (!attachments.isEmpty()) {

            var urls = attachments.stream()
                    .map(Message.Attachment::getProxyUrl)
                    .collect(Collectors.joining(", "));

            embed.addField("Attachments", urls, false);
        }

        var stickers = message.getStickers();

        if (!stickers.isEmpty()) {
            var names = stickers.stream()
                    .map(sticker -> MarkdownUtil.maskedLink(sticker.getName(), sticker.getStickerUrl()))
                    .collect(Collectors.joining(", "));

            embed.addField("Stickers", names, false);
        }

        if (!message.getContentRaw()
                .isEmpty()) {

            embed.addField("Content", message.getContentRaw(), true);

        }

        modLog.sendMessage(embed.build())
                .queue();


    }
}
