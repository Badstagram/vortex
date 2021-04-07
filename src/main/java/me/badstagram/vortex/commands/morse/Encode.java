package me.badstagram.vortex.commands.morse;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.SubCommand;
import me.badstagram.vortex.commandhandler.context.impl.SubCommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;

public class Encode extends SubCommand {
    public Encode() {
        this.category = new Category("Morse");

    }

    @Override
    public void execute(SubCommandContext ctx) throws BadArgumentException, CommandExecutionException {


        var args = ctx.getArgs();

        if (args.isEmpty())
            throw new BadArgumentException("text");

        try {
            var rawText = String.join("/", args);

            var split = rawText.split("");

            var sb = new StringBuilder("```");
            for (var letter : split) {
                var morse = MorseCode.fromLetter(letter);

                if (morse ==  null)
                    continue;

                sb.append(morse.getMorse());
            }

            var converted = sb.append("```")
                    .toString();

            ctx.getChannel()
                    .sendMessage(converted)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }


    }
}
