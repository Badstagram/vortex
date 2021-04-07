package me.badstagram.vortex.commands.config.set;

import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.EmbedUtil;

import java.util.Arrays;

public class AntiInvite extends SubCommand {


    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var validOpts = new String[] {"enable", "disable"};
        var settingsMgr = new GuildSettingsManager(ctx.getGuild().getId());
        if (ctx.getArgs().isEmpty() || Arrays.stream(validOpts)
                .noneMatch(opt -> opt.equalsIgnoreCase(ctx.getArgs().get(0)))) {

            var embed = EmbedUtil.createDefault()
                    .setTitle("Anti Invite")
                    .setDescription("Anti Invite is currently %s"
                            .formatted(settingsMgr.isAntiAdvertiseEnabled() ? "enabled" : "disabled"))
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();
            return;
        }


        new GuildSettingsManager(ctx.getGuild().getId())
                .setAntiAdvertiseEnabled(ctx.getArgs().get(0).equals("enable"));

        var embed = EmbedUtil.createDefault()
                .setTitle("Anti Invite")
                .setDescription("Anti Invite has been  %s"
                        .formatted(ctx.getArgs().get(1).equalsIgnoreCase("enable") ? "enabled" : "disabled"))
                .build();

        ctx.getChannel()
                .sendMessage(embed)
                .queue();

    }
}
