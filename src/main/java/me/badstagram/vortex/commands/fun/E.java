package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.CommandExecutionException;

import java.util.Arrays;
import java.util.List;

public class E extends Command {
    public E() {
        this.name = "e";
        this.help = "sometimes you just gotta E";
    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException {
        try {

            ctx.getChannel()
                    .sendMessage(this.getE())
                    .queue();
        } catch (Exception exception) {
            throw new CommandExecutionException(exception);
        }

    }

    protected String getE() {
        var e = List.of("""
                    ```
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEE
                    EEEEEEEEEEE
                    EEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEE
                    EEEEEEEEEEE
                    EEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    EEEEEEEEEEEEEEEEEEEEEE
                    ```
                """, "\uD83C\uDDEA", String.valueOf(Math.E));

        var rnd = Vortex.getRandom();

        return e.get(rnd.nextInt(e.size()));
    }
}
