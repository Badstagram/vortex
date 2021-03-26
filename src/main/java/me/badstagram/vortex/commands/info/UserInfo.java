package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.ArgumentParser;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import net.explodingbush.ksoftapi.entities.Ban;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfo extends Command {
    private final DateTimeFormatter dtf;

    public UserInfo() {
        this.name = "info";
        this.help = "Get info about a snowflake";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};

        this.dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException {
        try {
            var args = ctx.getArgs();

            User user;
            Member member;
            List<User> parsedUsers;

            if (ctx.getArgs()
                    .isEmpty()) {
                member = ctx.getMember();
                user = member.getUser();
            } else {

                var parser = new ArgumentParser(args, ctx.getMessage(), ctx.getJDA(), ctx.getGuild());

                parsedUsers = parser.parseUser();

                user = parsedUsers.isEmpty() ? ctx.getAuthor() : parsedUsers.get(0);
                member = ctx.getGuild()
                        .getMember(user);
            }


            var embed = member == null ? this.renderUserEmbed(user) : this.renderMemberEmbed(member);

            ctx.getMessage()
                    .reply(embed)
                    .mentionRepliedUser(false)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    protected MessageEmbed renderUserEmbed(User user) {
        var flagStr = ArgumentParser.parseUserFlags(user);
        var now = OffsetDateTime.now();

        if (user.getIdLong() == 82198898843586560L) {
            flagStr += " <:night:766617791817318410>";
        }


        var secondsUserCreated = user.getTimeCreated()
                .until(now, ChronoUnit.SECONDS);

        var ban = this.getKsoftBanInfo(user.getId());
        var embed =  EmbedUtil.createDefault()
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setDescription(flagStr)
                .addField("User ID", user.getId(), false)
                .addField("Time Account Created", String.format("%s (%sago)", user.getTimeCreated()
                        .format(this.dtf), FormatUtil.secondsToTimeCompact(secondsUserCreated)), false)
                .addField("Global Banned", """
                            Vortex: Soon:tm:
                            KSoft.Si: %s
                        """.formatted(ban.exists() ? MarkdownUtil.maskedLink("Yes for %s".formatted(ban.getReason()), ban
                        .getProof()) : "No"), true);


        if (user.isBot()) {
            embed.addField("Invite Link", "https://discord.com/api/oauth2/authorize?client_id=%d&permissions=8&scope=bot".formatted(user.getIdLong()), false);
        }

        return embed.build();
    }

    protected MessageEmbed renderMemberEmbed(Member member) {
        var user = member.getUser();
        var flagStr = ArgumentParser.parseUserFlags(user);
        var now = OffsetDateTime.now();

        var activeOn = member.getActiveClients()
                .stream()
                .map(ClientType::getKey)
                .map(FormatUtil::capitlise)
                .collect(Collectors.joining(", "));


        if (user.getIdLong() == 82198898843586560L) {
            flagStr += " <:night:766617791817318410>";
        }

        if (member.isOwner()) {
            flagStr += " <:ownercrown:810687537592139826>";
        }

        var roles = "None";

        if (!member.getRoles()
                .isEmpty()) {
            roles = member.getRoles()
                    .stream()
                    .map(Role::getAsMention)
                    .collect(Collectors.joining(", "));
        }

        var permissions = member.getPermissions();
        var perms = "None";
        if (member.isOwner()) {
            perms = "Server Owner";
        } else if (member.hasPermission(Permission.ADMINISTRATOR)) {
            perms = "Administrator";
        } else if (!permissions.isEmpty()) {
            perms = (permissions.stream()
                    .map(Permission::getName)
                    .collect(Collectors.joining(", ")));
        }


        var secondsUserCreated = user.getTimeCreated()
                .until(now, ChronoUnit.SECONDS);
        var secondsMemberJoined = member.getTimeJoined()
                .until(now, ChronoUnit.SECONDS);

        var ban = this.getKsoftBanInfo(user.getId());

        var embedBuilder = EmbedUtil.createDefault()
                .setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl())
                .setDescription(flagStr)
                .addField("Mention (ID)", String.format("%s (%s)", user.getAsMention(), user.getId()), true)
                .addField("Time Account Created", String.format("%s (%sago)", user.getTimeCreated()
                        .format(this.dtf), FormatUtil.secondsToTimeCompact(secondsUserCreated)), true)
                .addField("Time Member Joined", String.format("%s (%sago)", member.getTimeJoined()
                        .format(this.dtf), FormatUtil.secondsToTimeCompact(secondsMemberJoined)), true)
                .addField("Global Banned", """
                            Vortex: Soon:tm:
                            KSoft.Si: %s
                        """.formatted(ban.exists() ? MarkdownUtil.maskedLink("Yes for %s".formatted(ban.getReason()), ban
                        .getProof()) : "No"), true)

                .addField("Roles", roles, true)
                .addBlankField(true)
                .addField("Permissions", perms, false);

        Vortex.getLogger()
                .debug("OnlineStatus == {}", member.getOnlineStatus());

        if (member.getOnlineStatus() != OnlineStatus.OFFLINE || !activeOn.isEmpty() || !member.getActiveClients()
                .isEmpty()) {
            embedBuilder.addField("Active On", activeOn, true);
        }


        var activities = member.getActivities();

        if (!activities.isEmpty()) {
            embedBuilder.addField("Status", this.getActivityName(activities.get(0)), true);
        }

        if (user.isBot()) {
            embedBuilder.addField("Invite Link", "https://discord.com/api/oauth2/authorize?client_id=%d&permissions=8&scope=bot".formatted(user.getIdLong()), false);
        }

        return embedBuilder.build();
    }


    protected Ban getKsoftBanInfo(String userId) {

        return Vortex.getKSoftAPI()
                .getBan()
                .setUserId(userId)
                .execute();
    }

/*    protected GlobalBan getVortexBanInfo(String userId) {
        return GlobalBanManager.
    }*/

    protected String getActivityName(Activity activity) {

        var rp = activity.asRichPresence();

        if (rp == null)
            return activity.getName();

        return switch (activity.getType()) {
            case LISTENING -> "Listening to %s by %s".formatted(rp.getDetails(), rp.getState());
            case WATCHING -> "Watching %s".formatted(activity.getName());
            case STREAMING -> "Streaming %s".formatted(rp.getDetails());
            case COMPETING -> "Competing in %s".formatted(activity.getName());
            default -> activity.getName();
        };



/*        var type = activity.getType();

        if (type == Activity.ActivityType.LISTENING) {
            var rp = activity.asRichPresence();

            return "Listening to %s by %s".formatted(rp.getDetails(), rp.getState());
        } else if (type == )

        return "%s %s".formatted(FormatUtil.capitlise(activity.getType()
                .name()
                .toLowerCase()
                .replaceAll("_", "")), activity.getName());*/
    }
}
