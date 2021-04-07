package me.badstagram.vortex.commands.economy;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.managers.EconomyManager;
import me.badstagram.vortex.util.Checks;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;


public class Pay extends Command {
    public Pay() {
        this.name = "pay";
        this.help = "Pay another member";
        this.usage = "pay <member> <amount>";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("Economy");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("member", true);
        if (args.size() < 2) throw new BadArgumentException("amount", true);

        var parser = ctx.createArgumentParser();

        var parsedMembers = parser.parseMember();

        if (parsedMembers.isEmpty()) throw new BadArgumentException("member", false);

        var amt = args.get(1);

        var target = parsedMembers.get(0);

        if (!Checks.isValidInt(amt)) throw new BadArgumentException("amount", false);

        try {
            var amount = Integer.parseInt(amt);

            var payerBalance = new EconomyManager(ctx.getMember().getId(), ctx.getGuild().getId())
                    .getMoney();

            if (amount > payerBalance) {
                var embed = EmbedUtil.createDefaultError()
                        .setTitle("Transaction Failed")
                        .setDescription("You do not have enough money for this.")
                        .build();

                ctx.getChannel()
                        .sendMessage(embed)
                        .queue();
                return;
            }

            new EconomyManager(target.getId(), ctx.getGuild().getId())
                    .addMoney(amount);

            var embed = EmbedUtil.createDefaultError()
                    .setTitle("Transaction Successful")
                    .setDescription("You paid %d to %#s.".formatted(amount, target.getUser()))
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }
}
