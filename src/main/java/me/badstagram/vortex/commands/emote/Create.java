package me.badstagram.vortex.commands.emote;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Icon;

import java.net.URL;

public class Create extends Command {
    public Create() {
        this.name = "create";
        this.help = "Create an emote";
        this.botPermissions = new Permission[]{Permission.MANAGE_EMOTES};
        this.userPermissions = new Permission[]{Permission.MANAGE_EMOTES};

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        var args = ctx.getArgs();

        if (args.isEmpty() || args.size() < 2)
            throw new BadArgumentException("name");

        try {
            var name = args.get(0);
            var url = new URL(args.get(1));

            ctx.getGuild()
                    .createEmote(name, Icon.from(url.openStream()))
                    .queue(emote -> {
                        var embed = EmbedUtil.createDefault()
                                .setTitle("Emote created")
                                .addField("ID", emote.getId(), true)
                                .addField("Name", emote.getName(), true)
                                .addField("Mention", emote.getAsMention(), true)
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
