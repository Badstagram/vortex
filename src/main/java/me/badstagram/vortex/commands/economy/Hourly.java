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

public class Hourly extends Command {
    public Hourly() {
        this.name = "hourly";
        this.help = "Claim your daily reward";
        this.cooldown = Convert.toHours(1);
        this.category = new Category("Economy");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        try {
            var ecoManager = new EconomyManager(ctx.getAuthor().getId(), ctx.getGuild().getId());

            var reward = Vortex.getRandom()
                    .nextInt(250);

            var newBal = ecoManager.addMoney(reward);
            var embed = EmbedUtil.createDefault()
                    .setTitle("Hourly Reward")
                    .setDescription("You claimed your hourly reward of $" + reward)
                    .setFooter("You now have $"+newBal)
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
