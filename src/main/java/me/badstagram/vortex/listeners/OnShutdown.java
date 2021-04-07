package me.badstagram.vortex.listeners;

import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnShutdown extends ListenerAdapter {

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        Vortex.getLogger().error("Vortex has shutdown");

        Vortex.setReady(false);
    }
}
