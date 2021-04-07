package me.badstagram.vortex.commands.economy;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.EconomyManager;
import me.badstagram.vortex.util.Convert;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

public class Rob extends Command {
    public Rob() {
        this.name = "rob";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.cooldown = Convert.toMinutes(30);
        this.category = new Category("Economy");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var parsedMembers = ctx.createArgumentParser()
                .parseMember();

        if (parsedMembers.isEmpty()) throw new BadArgumentException("member", true);

        try {
            var target = parsedMembers.get(0);

            var rng = Vortex.getRandom();

            var ecoManagerMember = new EconomyManager(ctx.getMember().getId(), ctx.getGuild().getId());
            var ecoManagerTarget = new EconomyManager(target.getId(), ctx.getGuild().getId());

            var chance = rng.nextInt(5);

            if (chance == 1) {
                this.robSuccess(ctx.getChannel(), Math.round(rng.nextInt(ecoManagerTarget.getMoney() / 50)));
            } else {
                this.robFailed(ctx.getChannel(), Math.round(rng.nextInt(ecoManagerTarget.getMoney() / 50)));
            }


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }


    }

    void robFailed(TextChannel channel, int amount) {

    }

    void robSuccess(TextChannel channel, int amount) {

    }
}
