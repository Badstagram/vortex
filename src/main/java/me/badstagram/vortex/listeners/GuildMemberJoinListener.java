package me.badstagram.vortex.listeners;

import me.badstagram.vortex.automod.AntiRaid;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberJoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        var user = event.getUser();
        var member = event.getMember();
        var self = event.getGuild().getSelfMember();
        var settingsMgr = new GuildSettingsManager(event.getGuild());

        if (!user.getName().matches("(?i)[\\w\\d\\s]")) {
            if (!settingsMgr.isDeCancerEnabled()) return;

            if (!self.hasPermission(Permission.NICKNAME_MANAGE) || !self.canInteract(member)) return;

            member.modifyNickname("cleaned").queue();
        }

        try {
            DatabaseUtils.execute("INSERT INTO economy (user_id, guild_id, money, multiplier, multi) VALUES (?, ?, 0, DEFAULT, DEFAULT) ON CONFLICT DO NOTHING ",
                    event.getGuild().getId(), member.getId());

            DatabaseUtils.execute("INSERT INTO economy (user_id, guild_id, money, multiplier, multi) VALUES (?, ?, 0, DEFAULT, DEFAULT) ON CONFLICT DO NOTHING ",
                    event.getGuild().getId(), member.getId());
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }

        new AntiRaid(event).checkUser();
    }
}
