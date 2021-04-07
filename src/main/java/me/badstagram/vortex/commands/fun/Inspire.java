package me.badstagram.vortex.commands.fun;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import okhttp3.HttpUrl;
import okhttp3.Request;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Inspire extends Command {
    public Inspire() {
        this.name = "inspire";
        this.help = "Get inspired";
        this.category = new Category("Fun");

    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var url = this.getInspirationImage();

            if (url == null) {
                ctx.getChannel()
                        .sendMessage("Something went wrong while trying to inspire you :(")
                        .queue();
                return;
            }

            var embed = EmbedUtil.createDefault()
                    .setImage(url.toString())
                    .build();

            ctx.getChannel()
                    .sendMessage(embed)
                    .queue();

        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    @Nullable
    private URL getInspirationImage() throws IOException {
        var client = Vortex.getHttpClient();

        var url = new HttpUrl.Builder()
                .scheme("https")
                .host("inspirobot.me")
                .addPathSegment("api")
                .addQueryParameter("generate", "true")
                .build();

        var req = new Request.Builder()
                .url(url)
                .build();

        var res = client.newCall(req)
                .execute();

        var body = res.body();

        if (body == null || !res.isSuccessful())
            return null;

        try {
           return new URL(body.string());
        } catch (Exception ignored) {
            return null;
        }
    }
}
