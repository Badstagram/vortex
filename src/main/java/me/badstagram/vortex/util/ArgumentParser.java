package me.badstagram.vortex.util;

import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentParser {

    private final Message msg;
    private final JDA jda;
    private final Guild guild;
    private final List<String> args;


    public ArgumentParser(List<String> args, Message msg, JDA jda, Guild guild) {
        this.msg = msg;
        this.jda = jda;
        this.guild = guild;
        this.args = args;
    }

    public ArgumentParser(CommandContext ctx) {
        this.msg = ctx.getMessage();
        this.jda = ctx.getJDA();
        this.guild = ctx.getGuild();
        this.args = ctx.getArgs();
    }

    public static String parseUserFlags(@Nonnull User user) {

        StringBuilder sb = new StringBuilder();

        for (User.UserFlag flag : user.getFlags()) {
            switch (flag) {
                case BUG_HUNTER_LEVEL_1:
                    sb.append("<:bug_hunter:718646399297781790> ");
                    break;
                case BUG_HUNTER_LEVEL_2:
                    sb.append("<:bughuntergold:780917166274904114> ");
                    break;
                case EARLY_SUPPORTER:
                    sb.append("<:early_supporter:718646172549644298> ");
                    break;
                case HYPESQUAD:
                    sb.append("<:hypesquadevents:742397033461186690> ");
                    break;
                case HYPESQUAD_BALANCE:
                    sb.append("<:hypesquad_balance:718645777718575134> ");
                    break;
                case HYPESQUAD_BRAVERY:
                    sb.append("<:hypesquad_bravery:718645870622670921> ");
                    break;
                case HYPESQUAD_BRILLIANCE:
                    sb.append("<:hypesquad_brilliance:718646051422208010> ");
                    break;
                case PARTNER:
                    sb.append("<:partner:749386164523237459> ");
                    break;
                case STAFF:
                    sb.append("<:discord_staff:718646268917710918> ");
                    break;
                case SYSTEM:
                    sb.append("<:discord:749388843752358049> ");
                    break;
                case VERIFIED_BOT:
                case VERIFIED_DEVELOPER:
                    sb.append("<:verified:718646647005118485> ");
                    break;
                default:
                    break;
            }


        }

        // hacky workaround to see if a user has nitro
        if (user.getAvatarId() != null && user.getEffectiveAvatarUrl()
                .endsWith(".gif") && user.getAvatarId()
                .startsWith("a_")) {
            sb.append(" <:nitro:768055945280094208>");
        }

        if (user.isBot()) {
            sb.append(" <:bot_tag:762396082905940008>");
        }

        var creationTime = user.getTimeCreated().toEpochSecond();
        var timeInFuture = OffsetDateTime.now().plus(1, ChronoUnit.MONTHS).toEpochSecond();

        Vortex.getLogger().debug("creationTime: {}", creationTime);
        Vortex.getLogger().debug("timeInFuture: {}", timeInFuture);

        if (creationTime > timeInFuture) {
            sb.append("\uD83D\uDC76 "); // 👶
        }
        return sb.toString();
    }

    @Nonnull
    public List<User> parseUser() {

        List<User> users = new ArrayList<>();

        if (!this.msg.getMentionedMembers()
                .isEmpty()) {
            users.addAll(this.msg.getMentionedMembers()
                    .stream()
                    .map(Member::getUser)
                    .collect(Collectors.toList()));
        }

        for (var arg : this.args) {
            if (arg.matches("\\d+")) {

                try {
                    users.add(jda.retrieveUserById(arg).complete());
                } catch (ErrorResponseException ignored) {
                }
                continue;
            }

            users.addAll(this.jda.getUsersByName(arg, true));

        }
        return users;
    }

    @Nonnull
    public List<Member> parseMember() {
        List<Member> members = new ArrayList<>();
        if (!this.msg.getMentionedMembers()
                .isEmpty()) {
            members.addAll(this.msg.getMentionedMembers());
        }

        for (var arg : this.args) {
            members.addAll(this.guild.getMembersByEffectiveName(arg, true));
            var membersFromName = guild.getMembers()
                    .stream()
                    .filter(m -> m.getEffectiveName()
                            .equalsIgnoreCase(arg))
                    .collect(Collectors.toList());

            var membersFromTag = guild.getMembers()
                    .stream()
                    .filter(m -> m.getUser()
                            .getAsTag()
                            .equalsIgnoreCase(arg))
                    .collect(Collectors.toList());
            members.addAll(membersFromName);
            members.addAll(membersFromTag);


            if (arg.matches("\\d+")) {
                var member = this.guild.retrieveMemberById(arg).complete();

                members.add(member);

            }

        }
        return members;
    }

    @Nullable
    public List<Role> parseRole() {
        List<Role> roles = new ArrayList<>();
        if (!this.msg.getMentionedRoles()
                .isEmpty()) {
            roles.addAll(this.msg.getMentionedRoles());
        }

        for (var arg : this.args) {
            if (arg.matches("\\d+")) {
                roles.add(this.guild.getRoleById(arg));
            } else {
                roles.addAll(this.guild.getRolesByName(arg, true));
            }
        }

        return roles;
    }

    @Nullable
    public List<TextChannel> parseChannel() {
        List<TextChannel> channels = new ArrayList<>();
        if (!this.msg.getMentionedMembers()
                .isEmpty()) {
            channels.addAll(this.msg.getMentionedChannels());
        }


        for (var arg : this.args) {
            if (arg.matches("\\d+")) {
                channels.add(this.guild.getTextChannelById(arg));
            } else {
                channels.addAll(this.guild.getTextChannelsByName(arg, true));
            }
        }

        return channels;
    }


}
