package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class InviteLookup extends Command {
    public InviteLookup() {
        this.name = "lookup";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.help = "Lookup a server by an invite code";
        this.usage = "lookup <invite_code>";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var args = ctx.getArgs();

        if (args.isEmpty()) {
            throw new BadArgumentException("invite_code", true);
        }

        try {
            var invites = ctx.getMessage().getInvites();

            var code = invites.isEmpty() ? args.get(0) : invites.get(0);

            Invite.resolve(ctx.getJDA(), code, true)
                    .submit()
                    .whenCompleteAsync((invite, err) -> {
                        var guild = invite.getGuild();
                        var channel = invite.getChannel();
                        var creator = invite.getInviter();

                        if (err != null) {
                            ctx.getChannel()
                                    .sendMessage(MarkdownUtil.codeblock(err.getMessage()))
                                    .queue();
                            return;
                        }

                        if (guild == null && invite.getGroup() == null) {
                            ctx.getChannel()
                                    .sendMessage("```Unknown invite```")
                                    .queue();
                            return;
                        }

                        var embed = invite.getGroup() == null ? this.renderGuildEmbed(guild, channel, creator) : this.renderGroupEmbed(invite.getGroup(), creator);


                        ctx.getChannel()
                                .sendMessage(embed)
                                .queue();


                    });
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    protected MessageEmbed renderGuildEmbed(Invite.Guild guild, Invite.Channel channel, User creator) {
        var features = guild.getFeatures()
                .stream()
                .map(feature -> feature.replace("_", " "))
                .map(String::toLowerCase)
                .map(FormatUtil::capitlise)
                .collect(Collectors.joining(", "));


        return EmbedUtil.createDefault()
                .setAuthor("%s (%s)".formatted(guild.getName(), guild.getId()), guild.getIconUrl())
                .addField("Channel", "%s (%s)".formatted(channel.getName(), channel.getId()), false)
                .addField("Creator", "%s (%s)".formatted(creator == null ? "Unknown" : creator.getAsTag(), creator == null ? "Unknown" : creator.getId()), false)
                .addField("Guild Creation Date", guild.getTimeCreated().format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")), false)
                .addField("Verification Level", guild.getVerificationLevel().name(), false)
                .addField("Verification Level", features, false)
                .addField("Members", String.valueOf(guild.getMemberCount()), true)
                .addField("Of Which Online", String.valueOf(guild.getOnlineCount()), true)
                .build();
    }

    protected MessageEmbed renderGroupEmbed(Invite.Group group, User creator) {

        var users = group.getUsers();
        return EmbedUtil.createDefault()
                .setAuthor(group.getName(), group.getIconUrl())
                .addField("Creator", "%s (%s)".formatted(creator == null ? "Unknown" : creator.getAsTag(), creator == null ? "Unknown" : creator.getId()), false)
                .addField("Group Creation Date", group.getTimeCreated().format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")), false)
                .addField("Members (%d)".formatted(users.size()), String.join(", ", users), true)
                .build();
    }
}
