package me.badstagram.vortex.listeners;

import me.badstagram.vortex.core.Colors;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.Checks;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnUserAvatarUpdate extends ListenerAdapter {

    @Override
    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {
        var oldAvatar = event.getOldAvatarUrl();
        var newAvatar = event.getNewAvatarUrl();

        for (var guild : event.getUser().getMutualGuilds()) {
            var settingsMgr = new GuildSettingsManager(guild.getId());

            var modLogId = settingsMgr.getModLogChannel();

            if (modLogId == null)
                continue;

            var modLog = guild.getTextChannelById(modLogId);


            if (modLog == null || !Checks.canEmbedLinks(modLog, guild.getSelfMember()))
                continue;

            var discrim = Integer.parseInt(event.getUser().getDiscriminator());

            var embed = new EmbedBuilder()
                    .setColor(Colors.WARN.getAsColor())
                    .setTitle("User avatar changed.")
                    .addField("User", event.getUser().getAsTag(), false)
                    .setDescription("Image is new avatar, thumbnail is old avatar")
                    .setImage(newAvatar == null ? "https://cdn.discordapp.com/embed/avatars/%d.png".formatted(discrim % 5) : newAvatar)
                    .setThumbnail(oldAvatar == null ? "https://cdn.discordapp.com/embed/avatars/%d.png".formatted(discrim % 5) : oldAvatar)
                    .build();

            modLog.sendMessage(embed).queue();

        }


    }
}
