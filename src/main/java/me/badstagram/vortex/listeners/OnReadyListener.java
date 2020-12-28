package me.badstagram.vortex.listeners;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.jetbrains.annotations.NotNull;

public class OnReadyListener extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        var jda = event.getJDA();
        var guildsCount = jda.getGuilds()
                .size();

        for (var u : jda.getUsers())
            System.out.printf("%s (%s)%n", u.getAsTag(), u.getId());

        jda.getPresence()
                .setActivity(Activity.watching("The messages of %d users".formatted(jda.getUsers().size())));

        jda.getPresence()
                .setStatus(OnlineStatus.ONLINE);
    }
}
