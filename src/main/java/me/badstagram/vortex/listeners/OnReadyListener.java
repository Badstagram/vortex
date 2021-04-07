package me.badstagram.vortex.listeners;

import me.badstagram.vortex.api.VortexAPI;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.util.ErrorHandler;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class OnReadyListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        var jda = event.getJDA();

        jda.getPresence()
                .setActivity(Activity.watching("The messages of %d users".formatted(jda.getUsers().size())));

        jda.getPresence()
                .setStatus(OnlineStatus.ONLINE);

        Vortex.getLogger().info("Starting API...");

        try {
            new VortexAPI(jda).start();
        } catch (IOException e) {
            ErrorHandler.handle(e);
        }

        Vortex.setReady(true);
    }
}
