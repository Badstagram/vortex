package me.badstagram.vortex.listeners;

import io.grpc.ClientCall;
import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnGuildReady extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        var guild = event.getGuild();

        Vortex.getLogger()
                .info("Guild {} ({}) has become ready!", guild.getName(), guild.getId());


    }
}
