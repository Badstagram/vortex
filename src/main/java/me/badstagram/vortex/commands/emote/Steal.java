package me.badstagram.vortex.commands.emote;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.requests.RestAction;
import okhttp3.OkHttpClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Steal extends Command {
    public Steal() {
        this.name = "steal";
        this.help = "Steal emotes from another server";
        this.usage = "steal <emote1> [emote2]...";
        this.botPermissions = new Permission[]{Permission.MANAGE_EMOTES};
        this.userPermissions = new Permission[]{Permission.MANAGE_EMOTES};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var emotes = ctx.getMessage().getEmotes();

        if (emotes.isEmpty()) throw new BadArgumentException("emotes", true);

        try {


            var restActions = new ArrayList<RestAction<Emote>>();

            var emotesToSteal = emotes.stream()
                    .filter(em -> !em.isManaged()) // Prevent stealing Twitch Sub emotes
                    .collect(Collectors.toList());

            for (var emote : emotesToSteal) {
                restActions.add(ctx.getGuild()
                        .createEmote(emote.getName(), Icon.from(new URL(emote.getImageUrl()).openStream())));
            }

            RestAction.allOf(restActions)
                    .queue(emotesAdded -> {
                        var emoteMentions = emotesAdded.stream()
                                .map(Emote::getAsMention)
                                .collect(Collectors.joining(" "));

                        var embed = EmbedUtil.createDefault()
                                .setTitle("%d Emote%s Stolen".formatted(emotesAdded.size(), emotesAdded.size() == 1 ? "" : "s"))
                                .addField("Emote%s Stolen".formatted(emotesAdded.size() == 1 ? "" : "s"), emoteMentions, false)
                                .build();

                        ctx.getChannel()
                                .sendMessage(embed)
                                .queue();
                    });
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
