package me.badstagram.vortex.listeners;

import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class OnGuildJoin extends ListenerAdapter {


    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        var sql = "INSERT INTO guild_config (guild_id) VALUES (?) ON CONFLICT DO NOTHING";

        try {
            DatabaseUtils.execute(sql, event.getGuild().getId());
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }


    }
}
