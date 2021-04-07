package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Config;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Define extends Command {
    public Define() {
        this.name = "define";
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {

        var args = ctx.getArgs();

/*        if (args.isEmpty()) {
            throw new BadArgumentException("word", true);
        }*/

        try {
            var word = String.join(" ", args);
            var definitions = this.getDefinition(word);
            var sb = new StringBuilder();
            int i = 1;

            if (definitions == null) {
                ctx.getChannel()
                        .sendMessage("Something went wrong while getting the definition. Try again later.")
                        .queue();
                return;
            }

            for (var definition : definitions) {
                sb.append("%d. %s".formatted(i, definition));
                i++;
            }

            var embed = EmbedUtil.createDefault()
                    .setTitle(word)
                    .setDescription(sb.toString())
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();
        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    List<String> getDefinition(@Nonnull String word) throws Exception {

        var url = new HttpUrl.Builder()
                .scheme("https")
                .host("od-api.oxforddictionaries.com")
                .addEncodedPathSegments("api/v2/entries/en-gb")
                .addPathSegment(word)
                .build();

        var req = new Request.Builder()
                .url(new URL(""))
                .addHeader("Accept", "application/json")
                .addHeader("app_id", Config.get("oxford_app_id"))
                .addHeader("app_key", Config.get("oxford_app_key"))
                .build();

        var res = Vortex.getHttpClient()
                .newCall(req)
                .execute();

        super.getLogger().debug("Response code: {}", res.code());

        if (!res.isSuccessful()) return null;

        var entries = new JSONObject(res.body().string())
                .getJSONArray("results")
                .getJSONObject(0)
                .getJSONArray("lexicalEntries")
                .getJSONObject(0)
                .getJSONArray("entries")
                .getJSONObject(0)
                .getJSONArray("senses");

        List<String> definitions = new ArrayList<>();

        for (var i = 0; i < entries.length(); i++) {
            var definition = entries.getJSONObject(i)
                    .getJSONArray("definitions");

            for (var j = 0; i < definition.length(); i++) {
                definitions.add(definition.getString(j));
            }

        }

        return definitions;

    }
}
