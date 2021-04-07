package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CantPunishException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.ASCIIArtGenerator;
import net.dv8tion.jda.api.utils.MarkdownUtil;

public class AsciiArt extends Command {
    public AsciiArt() {
        this.name = "asciiart";
        this.aliases = new String[]{"ascii", "art"};
        this.help = "Generate ASCII art.";
        this.usage = "asciiart <character> <text>";
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException, CantPunishException {
        var args = ctx.getArgs();

        if (args.isEmpty() || args.size() < 3) {
            throw new BadArgumentException("size");
        }
        var size = args.get(0);
        var character = args.get(1);
        var text = String.join(" ", args.subList(2, args.size()));

        character = character.split("")[0];


        try {
            var artGen = new ASCIIArtGenerator();

            ctx.getChannel()
                    .sendMessage(MarkdownUtil.codeblock(null, artGen.printTextArt(text, ASCIIArtGenerator.ART_SIZE_SMALL, ASCIIArtGenerator.ASCIIArtFont.ART_FONT_MONO, character)))
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }
}
