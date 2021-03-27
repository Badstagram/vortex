package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Emotes;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.stream.Collectors;

public class Server extends Command {
    public Server() {
        this.name = "server";
        this.help = "Get info about the server";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var guild = ctx.getGuild();
            var members = guild.getMembers();

            var onlineMembers = members.stream()
                    .filter(m -> m.getOnlineStatus() == OnlineStatus.ONLINE)
                    .count();

            var idleMembers = members.stream()
                    .filter(m -> m.getOnlineStatus() == OnlineStatus.IDLE)
                    .count();

            var dndMembers = members.stream()
                    .filter(m -> m.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB)
                    .count();

            var offlineMembers = members.stream()
                    .filter(m -> m.getOnlineStatus() == OnlineStatus.OFFLINE)
                    .count();

            var bots = members.stream()
                    .map(Member::getUser)
                    .filter(User::isBot)
                    .count();

            var humans = members.stream()
                    .map(Member::getUser)
                    .filter(u -> !u.isBot())
                    .count();

            var embed = EmbedUtil.createDefault()
                    .setTitle(guild.getName())
                    .addField("ID", guild.getId(), true)
                    .addField("Members", """
                            <:online:774743921560649810> %d
                            <:status_idle:774743953303535636> %d
                            <:do_not_disturb:774743995238318112> %d
                            <:status_offline:774744016699785247> %d
                                                        
                            <:bot:816279960750784552> %d
                            \uD83D\uDC71 %d
                                                        
                                                        
                                                        
                            """.formatted(onlineMembers, idleMembers, dndMembers, offlineMembers, bots, humans), true)
                    .addField("Features", this.parseFeatures(guild)
                            .isEmpty() ? "None" : this.parseFeatures(guild), false)
                    .addField("Assets", this.getAssets(guild), false)
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    protected String getAssets(Guild guild) {
        var icon = guild.getIconUrl();
        var banner = guild.getBannerUrl();
        var splash = guild.getSplashUrl();

        var assets = "";

        if (icon != null)
            assets += "[`Icon`](%s) ".formatted(icon);

        if (banner != null)
            assets += "[Banner](%s) ".formatted(banner);

        if (splash != null)
            assets += "[Splash](%s) ".formatted(splash);


        return assets;
    }

    protected String parseFeatures(Guild guild) {
        var features = guild.getFeatures();
        return """
                %s `Animated Icon`
                %s `Commerce`
                %s `Discoverable`
                %s `Member Verification Gate Enabled`
                %s `Partnered`
                %s `Vanity Url`
                %s `Banner`
                %s `Community`
                %s `Invite Splash`
                %s `News`
                %s `Preview`
                %s `Verified`
                %s `Welcome Screen Enabled`
                """.formatted(
                features.contains("ANIMATED_ICON") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("COMMERCE") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("DISCOVERABLE") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("MEMBER_VERIFICATION_GATE_ENABLED") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("PARTNERED") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("VANITY_URL") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("BANNER") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("COMMUNITY") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("INVITE_SPLASH") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("NEWS") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("PREVIEW") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("VERIFIED") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED,
                features.contains("WELCOME_SCREEN_ENABLED") ? Emotes.EMOTE_SWITCH_ENABLED : Emotes.EMOTE_SWITCH_DISABLED
        );


    }

    protected String getEmotes(Guild guild) {
        return guild.getEmotes()
                .stream()
                .map(Emote::getAsMention)
                .collect(Collectors.joining());
    }

}

