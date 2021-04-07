package me.badstagram.vortex.commands.config;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.commands.config.set.AntiInvite;
import me.badstagram.vortex.commands.config.set.InviteAction;
import me.badstagram.vortex.commands.info.Ping;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.GuildSettingsManager;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;

public class Set extends SubCommand {
    public Set() {
        this.name = "set";
        this.help = "Set a config option. Replace spaces with underscores. E.g `invite action` -> `invite_action`";
        this.usage = "config set <option> <value>";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.userPermissions = new Permission[] {Permission.MANAGE_SERVER};
        this.subCommands = new Command[] {new Ping()};
        this.category = new Category("Config");

    }

    @Override
    public void execute(SubCommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var values = new String[] {"invite_action", "anti_invite"};

        var args = ctx.getArgs();
        if (args.isEmpty()) throw new BadArgumentException("option", true);
        if (args.size() < 2) throw new BadArgumentException("value", true);

        var isValidOption = Arrays.stream(values)
                .anyMatch(setting -> setting.equalsIgnoreCase(args.get(0)));

        if (!isValidOption) throw new BadArgumentException("option", false);

        var settingsMgr = new GuildSettingsManager(ctx.getGuild().getId());

        try {
            var option = args.get(0);
            var value = args.get(1);

            switch (option) {
                case "invite_action" -> new InviteAction(value).execute(new SubCommandContext(ctx.getOldCtx(), 2));
                case "anti_invite" -> new AntiInvite().execute(new SubCommandContext(ctx.getOldCtx(), 2));


                default -> throw new BadArgumentException("option", false);
            }
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    private void sendConfigUpdatedEmbed(TextChannel channel) {
        var embed = EmbedUtil.createDefault()
                .setTitle("Success")
                .setDescription("Configuration updated.")
                .build();

        channel.sendMessage(embed)
                .queue();
    }
}
