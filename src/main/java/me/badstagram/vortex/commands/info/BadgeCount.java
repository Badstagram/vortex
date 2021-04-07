package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.util.stream.Collectors;

public class BadgeCount extends Command {
    public BadgeCount() {
        this.category = new Category("Info");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException {
        try {
            var users = ctx.getGuild()
                    .getMembers()
                    .stream()
                    .map(Member::getUser)
                    .collect(Collectors.toList());

            var employees = users.stream()
                    .filter(u -> u.getFlags().contains(User.UserFlag.STAFF))
                    .count();

            var bugHunters = users.stream()
                    .filter(u -> u.getFlags().contains(User.UserFlag.BUG_HUNTER_LEVEL_1) || u.getFlags().contains(User.UserFlag.BUG_HUNTER_LEVEL_2))
                    .count();

            var earlySupporter = users.stream()
                    .filter(u -> u.getFlags().contains(User.UserFlag.EARLY_SUPPORTER))
                    .count();

            var hypesquadEvents = users.stream()
                    .filter(u -> u.getFlags().contains(User.UserFlag.HYPESQUAD))
                    .count();

            var balance = users.stream()
                    .filter(u -> u.getFlags().contains(User.UserFlag.HYPESQUAD_BALANCE))
                    .count();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }
}
