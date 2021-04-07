package me.badstagram.vortex.commands.admin;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Load extends Command {
    public Load() {
        this.name = "load";
        this.help = "loads a command";
        this.usage = "load <command>";
        this.owner = true;
        this.category = new Category("Admin");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        var args = ctx.getArgs();

        if (args.isEmpty())
            throw new BadArgumentException("name");

        var name = args.get(0);


        List<Command> commands = new Reflections("me.badstagram.vortex.commands")
                .getSubTypesOf(Command.class)
                .stream()
                .map(c -> {
                    try {
                        return c.getDeclaredConstructor().newInstance();
                    } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                        ctx.getChannel()
                                .sendMessageFormat(":x: %s: %s", e.getClass().getSimpleName(), e.getMessage())
                                .queue();


                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(cmd -> cmd.getName().equals(name))
                .collect(Collectors.toList());

        if (commands.isEmpty())
            throw new CommandExecutionException(new IllegalArgumentException("No commands with that name found"));

        var cmd = commands.get(0);

        ctx.getClient()
                .getCommands()
                .put(name, cmd);

        var embed = EmbedUtil.createDefault()
                .setTitle("Command Unloaded")
                .setDescription("Command %s has been loaded".formatted(cmd.getName()))
                .build();

        ctx.getChannel()
                .sendMessage(embed)
                .queue();


    }

}
