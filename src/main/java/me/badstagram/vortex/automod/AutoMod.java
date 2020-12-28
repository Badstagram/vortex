package me.badstagram.vortex.automod;

import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.entities.GuildPunishmentType;
import me.badstagram.vortex.managers.GuildPunishmentManager;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.DatabaseUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;

public class AutoMod {

    private final Message msg;
    private final JDA jda;
    private final GuildSettingsManager settings;

    public AutoMod(Message msg) {
        this.msg = msg;
        jda = this.msg.getJDA();
        this.settings = new GuildSettingsManager(msg.getGuild().getId());
    }

    public void antiInvite() {
        var author = this.msg.getAuthor();
        var reason = "[Auto-Mod] Anti Invite";
        var msgGuild = this.msg.getGuild();

        if (!this.settings.isAntiAdvertiseEnabled()) return;

        var invites = this.msg.getInvites();

        if (invites.isEmpty()) return; // no invites in this message.

        for (var inv : invites) {
            Invite.resolve(this.msg.getJDA(), inv).queue(invite -> {
                var group = invite.getGroup();
                var guild = invite.getGuild();

                if (group != null || guild != null && guild.getIdLong() != this.msg.getGuild().getIdLong()) {
                    var action = this.settings.getAdvertiseAction();

                    switch (action) {
                        case BAN -> msgGuild.ban(author, 0).queue(v -> {
                            var caseId = new GuildPunishmentManager(this.msg.getAuthor().getId(),
                                    this.msg.getGuild().getId())
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
                            var caseId = new GuildPunishmentManager(this.msg.getAuthor().getId(),
                                    this.msg.getGuild().getId())
                                    .createCase(reason, GuildPunishmentType.WARN, "702129848910610452", true, null,
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

        var raw = this.msg.getContentRaw();
        var split = raw.split("\\s+");

        var filteredWords = Arrays.asList(((String) DatabaseUtils
                .executeQuery("SELECT word_filter FROM guild_config WHERE guild_id = ?", this.msg.getGuild().getId())
                .get("word_filter")).split(";"));

        var isFilteredWordPresent = filteredWords.stream()
                .anyMatch(raw::contains);



        if (!isFilteredWordPresent) {
            Vortex.getLogger().debug("Filtered word NOT present");
            return;
        };

        Vortex.getLogger().debug("Filtered word present");

        this.msg.delete()
                .reason("[Auto-Mod] Message Blocked by Message Filter")
                .queue();


    }
}
