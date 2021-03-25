package me.badstagram.vortex.listeners;

import me.badstagram.vortex.util.DatabaseUtils;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnGuildLeave extends ListenerAdapter {
    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        var sql = "DELETE FROM guild_config WHERE guild_id = ?";

        try {
            DatabaseUtils.execute(sql, event.getGuild().getId());
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }
}
