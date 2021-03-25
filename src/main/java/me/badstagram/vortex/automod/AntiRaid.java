package me.badstagram.vortex.automod;

import me.badstagram.vortex.entities.enums.AccountFlag;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AntiRaid {
    private final Guild guild;
    private final User user;

    public AntiRaid(User user, Guild guild) {
        this.user = user;
        this.guild = guild;
    }

    public AntiRaid(GuildMemberJoinEvent event) {
        this.user = event.getUser();
        this.guild = event.getGuild();
    }

    public void checkUser() {
        var antiRaidFlags = AccountFlag.getFlagsForUser(user);

        if (antiRaidFlags.isEmpty()) return; // account not suspicious

        var description = antiRaidFlags.stream()
                .map(flag -> "%s %s".formatted(flag.getEmoji(), flag.getName()))
                .collect(Collectors.joining("\n"));

        var embed = EmbedUtil.createDefaultError()
                .setTitle("Suspicious User Joined")
                .setDescription(description)
                .addField("Creation Date", user.getTimeCreated()
                        .format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")), true)
                .addBlankField(true)
                .addBlankField(true)
                .build();


        var modLogId = new GuildSettingsManager(this.guild.getId()).getModLogChannel();

        if (modLogId == null) return; // mod log not set

        var modLog = this.guild.getTextChannelById(modLogId);

        if (modLog == null) return; // mod log probably deleted

        if (guild.getSelfMember().hasPermission(modLog, Permission.MESSAGE_EMBED_LINKS))
            return; // bot can't send embeds in mod log channel

        modLog.sendMessage(embed).queue();

    }
}
