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
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

public class Work extends Command {
    public Work() {
        this.name = "work";
        this.help = "Go to work";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.cooldown = Convert.toMinutes(30);
        this.category = new Category("Economy");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {

            var job = this.getJob();


            var reward = Vortex.getRandom()
                    .nextInt(250);

            var embed = EmbedUtil.createDefault()
                    .setTitle("Work")
                    .setDescription("You worked as a **%s** and earned **$%d**".formatted(job, reward))
                    .setFooter("You can work again in %s".formatted(FormatUtil.secondsToTimeCompact(this.cooldown)))
                    .build();

            new EconomyManager(ctx.getAuthor()
                    .getId(), ctx.getGuild()
                    .getId())
                    .addMoney(reward);

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }


    protected String getJob() {
        var jobs = Arrays.asList(
                "Doctor",
                "Police Officer",
                "Postman",
                "Mcdonalds Worker",
                "KFC Manager",
                "Google CEO",
                "Pizza Delivery Driver",
                "Youtuber",
                "Actor",
                "Tesco Worker",
                "Software Engineer"
        );
        var rnd = Vortex.getRandom();
        return jobs.get(rnd.nextInt(jobs.size()));

    }
}
