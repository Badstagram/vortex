package me.badstagram.vortex.listeners;

import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnGuildAvailable extends ListenerAdapter {

    @Override
    public void onGuildAvailable(@NotNull GuildAvailableEvent event) {
        var guild = event.getGuild();

        Vortex.getLogger().info("Guild {} ({}) has become available!", guild.getName(), guild.getId());
    }
}
