package me.badstagram.vortex.listeners;

import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class OnGuildMemberJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        var sql = "INSERT INTO economy (user_id, guild_id, money) VALUES (?, ?, 0)";

        try {
            DatabaseUtils.execute(sql, event.getGuild()
                    .getId());
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }
}
