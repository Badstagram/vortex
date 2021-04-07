package me.badstagram.vortex.commands.info;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Config;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;

public class TranslateCommand extends Command {
    public TranslateCommand() {
        this.name = "translate";
        this.help = "translate text";
        this.usage = "translate <language> <text>";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("Info");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var args = ctx.getArgs();

        if (args.isEmpty()) throw new BadArgumentException("language", true);
//        if (args.size() < 2) throw new BadArgumentException("text", true);


        try {
//            var lang = args.get(0);
            var text = String.join(" ", args);

            var translation = TranslateOptions.newBuilder()
                    .setApiKey(Config.get("google_translate_key"))
                    .setProjectId(Config.get("google_translate_project_id"))
                    .setTargetLanguage("en")
                    .build()
                    .getService();

            var result = translation.translate(text, Translate.TranslateOption.targetLanguage("en"));


            var embed = EmbedUtil.createDefault()
                    .addField("Source Language", result.getSourceLanguage(), false)
                    .addField("Translation", result.getTranslatedText(), false)
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }

    }
}
