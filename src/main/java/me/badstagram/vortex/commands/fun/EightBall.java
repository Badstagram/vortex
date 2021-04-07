package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Colors;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;

import java.awt.*;

public class EightBall extends Command {
    public EightBall() {
        this.name = "8ball";
        this.help = "Ask the magic 8 ball a question";
        this.usage = "8ball <question>";
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {


        if (ctx.getArgs().isEmpty()) {
            throw new BadArgumentException("question", true);
        }

        try {
            var question = String.join(" ", ctx.getArgs());
            var response = Responses.getRandom();

            var embed = EmbedUtil.createDefault()
                    .setColor(response.getColor())
                    .addField("Question", question, false)
                    .addField("Response", response.getResponse(), false)
                    .setThumbnail("https://media.discordapp.net/attachments/783473257139535936/783475286318252052/8ball.png")
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();

        } catch (Exception e) {

            throw new CommandExecutionException(e);
        }

    }
}

enum Responses {


    /* Positive Responses */

    IT_IS_CERTAIN("It is certain", Colors.SUCCESS),
    IT_IS_DECIDEDLY_SO("It is decidedly so", Colors.SUCCESS),
    WITHOUT_A_DOUBT("Without a doubt", Colors.SUCCESS),
    YES_DEFINITELY("Yes - Definitely", Colors.SUCCESS),
    AS_I_SEE_IT_YES("As I see it, yes", Colors.SUCCESS),
    MOST_LIKELY("Most likely", Colors.SUCCESS),
    YES("Yes", Colors.SUCCESS),

    /* Indecisive Responses */

    REPLY_HAZY_TRY_AGAIN("Reply hazy, try again", Colors.WARN),
    ASK_AGAIN_LATER("Ask again later", Colors.WARN),
    BETTER_NOT_TELL_YOU_NOW("Better not tell you now", Colors.WARN),
    CANNOT_PREDICT_NOW("Cannot predict now", Colors.WARN),

    /* Negative Responses */

    CONCENTRATE_AND_ASK_AGAIN("Concentrate and ask again", Colors.WARN),
    DONT_COUNT_ON_IT("Don't count on it", Colors.ERROR),
    MY_REPLY_IS_NO("My reply is no", Colors.ERROR),
    MY_SOURCES_SAY_NO("My sources say no", Colors.ERROR),
    OUTLOOK_NOT_SO_GOOD("Outlook not so good", Colors.ERROR),
    VERY_DOUBTFUL("Very doubtful", Colors.ERROR);

    private final String response;
    private final Color color;

    Responses(String response, Colors color) {
        this.response = response;
        this.color = color.getAsColor();
    }

    public String getResponse() {
        return response;
    }

    public Color getColor() {
        return color;
    }

    public static Responses getRandom() {
        var values = values();
        var pick = Vortex.getRandom()
                .nextInt(values.length);


        return values[pick];
    }
}

