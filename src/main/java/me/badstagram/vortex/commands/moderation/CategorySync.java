package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CategorySync extends Command {

    public CategorySync() {
        this.name = "sync";
        this.help = "Sync every channel with in the current channels category with the permissions of the category";
        this.botPermissions = new Permission[]{Permission.MANAGE_CHANNEL, Permission.MESSAGE_EMBED_LINKS};
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        this.category = new Category("Moderation");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var channel = ctx.getChannel();

        var parent = channel.getParent();

        if (parent == null) {
            channel.sendMessage("Channel has no category.").queue();
            return;
        }

        var waiter = Vortex.getWaiter();

        Predicate<GuildMessageReceivedEvent> check = e -> e.getChannel().getIdLong() == channel.getIdLong() && e.getAuthor().getIdLong() == ctx.getAuthor().getIdLong();
        waiter.waitForEvent(GuildMessageReceivedEvent.class, check, e -> {
            var channels = parent.getChannels()
                    .stream()
                    .filter(ch -> ch.getType() != ChannelType.CATEGORY)
                    .filter(ch -> !ch.isSynced())
                    .collect(Collectors.toList());


            for (var ch : channels) {
                ch.getManager()
                        .sync(parent)
                        .queue();
            }

            channel.sendMessageFormat("Successfully synced %d channels", channels.size())
                    .queue();
        });





    }
}
