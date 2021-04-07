package me.badstagram.vortex.automod;

import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.entities.enums.GuildPunishmentType;
import me.badstagram.vortex.managers.GuildPunishmentManager;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.Checks;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.MiscUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class AutoMod {

    private final Message msg;
    private final JDA jda;
    private final GuildSettingsManager settings;

    public AutoMod(Message msg) {
        this.msg = msg;
        jda = this.msg.getJDA();
        this.settings = new GuildSettingsManager(msg.getGuild()
                .getId());
    }

    public void antiInvite() {
        var author = this.msg.getAuthor();
        var reason = "[Auto-Mod] Anti Invite";
        var msgGuild = this.msg.getGuild();

        if (!this.settings.isAntiAdvertiseEnabled()) return;

        var invites = this.msg.getInvites();

        if (invites.isEmpty()) return; // no invites in this message.

        for (var inv : invites) {
            Invite.resolve(this.msg.getJDA(), inv)
                    .queue(invite -> {
                        var group = invite.getGroup();
                        var guild = invite.getGuild();

                        if (group != null || guild != null && guild.getIdLong() != this.msg.getGuild()
                                .getIdLong()) {
                            var action = this.settings.getAdvertiseAction();

                            switch (action) {
                                case BAN -> msgGuild.ban(author, 0)
                                        .queue(v -> {
                                            var caseId = new GuildPunishmentManager(this.msg.getAuthor()
                                                    .getId(),
                                                    this.msg.getGuild()
                                                            .getId())
                                                    .createCase(reason, GuildPunishmentType.BAN, "702129848910610452", true, null,
                                                            null);
                                            this.msg.getChannel()
                                                    .sendMessageFormat(":white_check_mark: %s has been banned for `%s` | Case #%d",
                                                            author.getAsTag(),
                                                            reason, caseId)
                                                    .queue();
                                        }, ignored -> this.msg.getChannel()
                                                .sendMessageFormat("I couldn't ban %#s. Do I have the correct permissions?", author)
                                                .queue());
                                case WARN -> {
                                    var caseId = new GuildPunishmentManager(this.msg.getAuthor()
                                            .getId(),
                                            this.msg.getGuild()
                                                    .getId())
                                            .createCase(reason, GuildPunishmentType.STRIKE, "702129848910610452", true, null,
                                                    null);
                                    this.msg.getChannel()
                                            .sendMessageFormat(":white_check_mark: %s has been warned for `%s` | Case #%d",
                                                    author.getAsTag(),
                                                    reason, caseId)
                                            .queue();
                                }

                                case IGNORE -> {
                                } // as the name suggests, do fuck all.


                            }
                        }

                    });
        }
    }

    public void wordFilter() throws Exception {
        var split = this.msg.getContentRaw()
                .split("\\s+");

        var wordStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("banned_words.txt");

        if (wordStream == null) {
            Vortex.getLogger()
                    .error("Something broke while reading the Word Filter list.");
            return;
        }

        var wordList = MiscUtil.readInputStream(wordStream);

        var bannedWords = Arrays.asList(wordList.split("\\s+"));
        

        if (!this.msg.getGuild()
                .getSelfMember()
                .hasPermission(Permission.MESSAGE_MANAGE)) return; // bot cant delete messages


        for (var word : split) {
            if (bannedWords.contains(word)) {
                this.msg.delete().queue();

                var punishmentMgr = new GuildPunishmentManager(this.msg.getMember());
                var settingsMgr = new GuildSettingsManager(this.msg.getGuild());

                var caseId = punishmentMgr.createCase("[Auto Mod] Global Banned Word", GuildPunishmentType.STRIKE, this.jda.getSelfUser()
                        .getId(), true, null, null);
                var strikeEmbedToUser = EmbedUtil.createDefaultWarning()
                        .setTitle("User Stricken")
                        .addField("Moderator", this.jda.getSelfUser()
                                .getAsTag(), true)
                        .addField("Reason", "[Auto Mod] Global Banned Word", true)
                        .addField("Guild", this.msg.getGuild()
                                .getName(), true)
                        .setFooter("Case %d".formatted(caseId))
                        .build();

                this.msg.getAuthor()
                        .openPrivateChannel()
                        .flatMap(ch -> ch.sendMessage(strikeEmbedToUser))
                        .queue();

                var punishLogId = settingsMgr.getPunishLogChannel();

                if (punishLogId == null) return;

                var punishLog = this.msg.getGuild().getTextChannelById(punishLogId);

                if (punishLog == null) return;

                var strikeEmbedToPunishLog = EmbedUtil.createDefaultWarning()
                        .setTitle("User Stricken")
                        .addField("Moderator", this.jda.getSelfUser()
                                .getAsTag(), true)
                        .addField("Reason", "[Auto Mod] Global Banned Word", true)
                        .setFooter("Case %d".formatted(caseId))
                        .build();

                if (!Checks.canEmbedLinks(punishLog, this.msg.getGuild().getSelfMember())) return;

                punishLog.sendMessage(strikeEmbedToPunishLog).queue();


            }
        }


    }

    public void checkDuplicate() {
        if (!this.msg.getChannel()
                .hasLatestMessage()) return; // channels latest message isnt cached

        if (!this.msg.getGuild()
                .getSelfMember()
                .hasPermission(Permission.MESSAGE_MANAGE)) return; // bot cant delete messages

        var latestMessageId = this.msg.getChannel()
                .getLatestMessageIdLong();

        this.msg
                .getChannel()
                .retrieveMessageById(latestMessageId)
                .queue(msg -> {

                    if (msg.getAuthor()
                            .getIdLong() == this.msg.getAuthor()
                            .getIdLong() && msg.getContentRaw()
                            .equalsIgnoreCase(this.msg.getContentRaw())) {
                        msg.delete()
                                .queue();

                        this.msg.getChannel()
                                .sendMessageFormat("%s, Don't post duplicate messages.", this.msg.getAuthor())
                                .queue();
                    }
                });
    }
}
