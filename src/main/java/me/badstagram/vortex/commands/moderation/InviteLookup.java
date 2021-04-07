package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.entities.enums.GuildVerificationLevel;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.time.format.DateTimeFormatter;

public class InviteLookup extends Command {
    public InviteLookup() {
        this.name = "lookup";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.help = "Lookup a server by an invite code";
        this.usage = "lookup <invite_code>";
        this.category = new Category("Moderation");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var args = ctx.getArgs();
        var invites = ctx.getMessage()
                .getInvites();

        if (args.isEmpty()) {
            throw new BadArgumentException("invite_code", true);
        }

        try {

            var code = invites.isEmpty() ? args.get(0) : invites.get(0);

            Invite.resolve(ctx.getJDA(), code, true)
                    .queue(invite -> {
                        var guild = invite.getGuild();
                        var group = invite.getGroup();
                        var channel = invite.getChannel();
                        var creator = invite.getInviter();

                        var embed = invite.getGroup() == null ? this.renderGuildEmbed(guild, channel, creator) : this.renderGroupEmbed(group, creator);


                        ctx.getChannel()
                                .sendMessage(embed)
                                .queue();
                    }, thr -> ctx.getChannel()
                            .sendMessage("Unknown Invite")
                            .queue());
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    protected MessageEmbed renderGuildEmbed(Invite.Guild guild, Invite.Channel channel, User creator) {
        var features = FormatUtil.parseGuildFeatures(guild);


        return EmbedUtil.createDefault()
                .setAuthor("%s (%s)".formatted(guild.getName(), guild.getId()))
                .setThumbnail(guild.getIconUrl())
                .addField("Channel", "%s (%s)".formatted(channel.getName(), channel.getId()), false)
                .addField("Creator", "%s (%s)".formatted(creator == null ? "Unknown" : creator.getAsTag(), creator == null ? "Unknown" : creator.getId()), false)
                .addField("Guild Creation Date", guild.getTimeCreated()
                        .format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")), false)
                .addField("Verification Level", GuildVerificationLevel.fromApiName(guild.getVerificationLevel()
                        .name())
                        .getHumanName(), false)
                .addField("Features", features, false)
                .addField("Members", String.valueOf(guild.getMemberCount()), true)
                .addField("Of Which Online", String.valueOf(guild.getOnlineCount()), true)
                .build();
    }

    protected MessageEmbed renderGroupEmbed(Invite.Group group, User creator) {

        var users = group.getUsers();
        return EmbedUtil.createDefault()
                .setAuthor(group.getName())
                .setThumbnail(group.getIconUrl())
                .addField("Creator", "%s (%s)".formatted(creator == null ? "Unknown" : creator.getAsTag(), creator == null ? "Unknown" : creator.getId()), false)
                .addField("Group Creation Date", group.getTimeCreated()
                        .format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")), false)
                .addField("Members (%d)".formatted(users.size()), String.join(", ", users), true)
                .build();
    }
}
