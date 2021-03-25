/*
 MIT License

 Copyright (c) 2020 badstagram

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.

 */

package me.badstagram.vortex.cache;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.RestAction;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MessageCache {
    private static final int SIZE = 1000;
    private final HashMap<Long, FixedCache<Long, CachedMessage>> cache = new HashMap<>();

    public CachedMessage putMessage(final Message m) {
        if (!cache.containsKey(m.getGuild().getIdLong())) {
            cache.put(m.getGuild().getIdLong(), new FixedCache<>(SIZE));
        }
        return cache.get(m.getGuild().getIdLong()).put(m.getIdLong(), new CachedMessage(m));
    }

    public CachedMessage pullMessage(Guild guild, long messageId) {
        if (!cache.containsKey(guild.getIdLong())) {
            return null;
        }
        return cache.get(guild.getIdLong()).pull(messageId);
    }

    public List<CachedMessage> getMessages(Guild guild, Predicate<CachedMessage> predicate) {
        if (!cache.containsKey(guild.getIdLong())) {
            return Collections.emptyList();
        }
        return cache.get(guild.getIdLong())
                .getValues()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param guild The guild
     * @param messageId The message ID
     * @return The cached message or {@code null} if the message ID is not cached
     */
    @Nullable
    public CachedMessage getMessageById(Guild guild, long messageId) {
        return this.getMessageById(guild.getIdLong(), messageId);
    }

    /**
     *
     * @param guildId The guild ID
     * @param messageId The message ID
     * @return The cached message or {@code null} if the message ID is not cached
     */
    @Nullable
    public CachedMessage getMessageById(long guildId, long messageId) {
        if (!cache.containsKey(guildId)) {
            return null;
        }
        var messages = cache.get(guildId)
                .getValues()
                .stream()
                .filter(msg -> msg.getIdLong() == messageId)
                .collect(Collectors.toList());

        return messages.isEmpty() ? null : messages.get(0);
    }


    public static class CachedMessage implements ISnowflake {
        private final String content, username, discriminator, jumpUrl;
        private final long id, author, channel, guild;
        private final List<Message.Attachment> attachments;
        private final List<MessageSticker> stickers;

        /**
         * Represents a {@link Message} cached by the bot.
         * @param message The old {@link Message}
         */
        private CachedMessage(Message message) {
            this.content = message.getContentRaw();
            this.id = message.getIdLong();
            this.author = message.getAuthor().getIdLong();
            this.username = message.getAuthor().getName();
            this.discriminator = message.getAuthor().getDiscriminator();
            this.channel = message.getChannel().getIdLong();
            this.guild = message.getGuild().getIdLong();
            this.attachments = message.getAttachments();
            this.stickers = message.getStickers();
            this.jumpUrl = message.getJumpUrl();
        }

        public String getContentRaw() {
            return this.content;
        }

        public List<Message.Attachment> getAttachments() {
            return attachments;
        }

        public User getAuthor(JDA jda) {
            return jda.getUserById(author);
        }

        public RestAction<User> retrieveAuthor(JDA jda) {
            return jda.retrieveUserById(this.author);
        }

        public String getUsername() {
            return username;
        }

        public String getDiscriminator() {
            return discriminator;
        }

        public long getAuthorId() {
            return author;
        }

        public TextChannel getTextChannel(JDA jda) {
            if (guild == 0L) {
                return null;
            }
            Guild g = jda.getGuildById(guild);
            if (g == null) {
                return null;
            }
            return g.getTextChannelById(channel);
        }

        public long getTextChannelId() {
            return channel;
        }

        public TextChannel getTextChannel(Guild guild) {
            return guild.getTextChannelById(channel);
        }

        public Guild getGuild(JDA jda) {
            if (guild == 0L) {
                return null;
            }
            return jda.getGuildById(guild);
        }

        @Override
        public long getIdLong() {
            return this.id;
        }

        public List<MessageSticker> getStickers() {
            return stickers;
        }

        public String getAsTag() {
            return "%s#%s".formatted(this.username, this.discriminator);
        }

        public String getJumpUrl() {
            return this.jumpUrl;
        }
    }
}

