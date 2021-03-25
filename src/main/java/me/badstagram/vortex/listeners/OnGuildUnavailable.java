package me.badstagram.vortex.listeners;

import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnGuildUnavailable extends ListenerAdapter {

    @Override
    public void onGuildUnavailable(@NotNull GuildUnavailableEvent event) {
        var guild = event.getGuild();

        Vortex.getLogger().warn("Guild {} ({}) has gone unavailable", guild.getName(), guild.getId());
    }
}
