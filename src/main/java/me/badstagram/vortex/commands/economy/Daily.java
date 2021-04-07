package me.badstagram.vortex.commands.economy;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.EconomyManager;
import me.badstagram.vortex.util.Convert;
import me.badstagram.vortex.util.EmbedUtil;

public class Daily extends Command {
    public Daily() {
        this.name = "daily";
        this.help = "Claim your daily reward";
        this.cooldown = Convert.toDays(1);
        this.category = new Category("Economy");

    }



    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var random = Vortex.getRandom();

            var reward = random.nextInt(1725);

            var ecoMgr = new EconomyManager(ctx.getAuthor().getId(), ctx.getGuild().getId());

            var newBalance = ecoMgr.addMoney(reward);

            var embed = EmbedUtil.createDefault()
                    .setTitle("Daily Reward")
                    .setDescription("You claimed your daily reward of $%d".formatted(reward))
                    .setFooter("Your new balance is $%d".formatted(newBalance))
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
