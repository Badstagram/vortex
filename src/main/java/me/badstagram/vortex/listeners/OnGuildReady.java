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
        var guildCount = event.getJDA().getGuilds().size();

        var readyGuilds = 0;

        Vortex.getLogger()
                .info("Guild {} ({}) has become ready!", guild.getName(), guild.getId());

        readyGuilds++;

        if (readyGuilds == guildCount) {
            Vortex.getLogger()
                    .info("All guilds are ready.");
        }
    }
}
