package me.badstagram.vortex.commands.moderation;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.ArgumentParser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MassBan extends Command {
    public MassBan() {
        this.name = "massban";
        this.help = "Ban many users at once";
        this.usage = "massban <user...>' | '|<reason>";
        this.botPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var args = ctx.getArgs();
            if (args.isEmpty()) {
                throw new BadArgumentException("user...", true);
            }

            var membersRaw = Arrays.asList(String.join(" ", args).split(" \\| ")).subList(1, args.size());

            var parser = new ArgumentParser(membersRaw, ctx.getMessage(), ctx.getJDA(), ctx.getGuild());

            var usersParsed = parser.parseUser();
            var reason = args.get(1);


            var ids = usersParsed.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());

            for (var id : ids) {
                ctx.getGuild()
                        .ban(id, 7)
                        .queue();
            }
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}