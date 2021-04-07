package me.badstagram.vortex.commands.economy;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.ArgumentParser;
import net.dv8tion.jda.api.Permission;

public class Balance extends Command {
    public Balance() {
        this.name = "balance";
        this.aliases = new String[] {"bal"};
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.help = "Get the balance of yourself or another member";
        this.usage = "balance [member]";
        this.category = new Category("Economy");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {


            var parser = new ArgumentParser(ctx.getArgs(), ctx.getMessage(), ctx.getJDA(), ctx.getGuild());

            var parsed = parser.parseMember();

            var member = parsed.isEmpty() ? ctx.getMember() : parsed.get(0);
            var id = member.getId();

            var ecoManager = ctx.getMember()
                    .getEconomyManager();

            var balance = ecoManager.getMoney();

            var embed = EmbedUtil.createDefault()
                    .setTitle("%s's Balance".formatted(member.getUser()
                            .getAsTag()))
                    .setDescription("You have $%d.".formatted(balance))
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
