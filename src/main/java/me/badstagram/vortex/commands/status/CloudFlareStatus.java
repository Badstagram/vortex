package me.badstagram.vortex.commands.status;

import com.jagrosh.jdautilities.menu.Paginator;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Colors;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;

public class CloudFlareStatus extends Command {
    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var components = this.getCloudFlareStatus();


        if (components == null) {
            ctx.reply("Something went wrong while fetching CloudFlare's status. Try again later.");
            return;
        }
        var format = "%s **%s**: %s\n";
        var sb = new StringBuilder();
        var pagenationBuilder = new Paginator.Builder()
                .setEventWaiter(Vortex.getWaiter())
                .setColor(Colors.RANDOM.getAsColor());

        for (int i = 0; i < components.length(); i++) {
            JSONObject component = components.getJSONObject(i);
            var componentName = component.getString("name");
            var componentStatus = component.getString("status");
            var emoji = this.toEmoji(componentStatus);

            sb.append(String.format(format, emoji, componentName, FormatUtil.capitlise(componentStatus)));
        }


        var embed = EmbedUtil.createDefault()
                .setTitle("CloudFlare's Status", "https://discordstatus.com")
                .setColor(ctx.getSelfMember().getColor())
                .setDescription(sb.toString())
                .build();

        ctx.getChannel().sendMessage(embed).queue();

    }

    @Nullable
    JSONArray getCloudFlareStatus() {
        try {
            OkHttpClient client = Vortex.getHttpClient();

            Request request = new Request.Builder()
                    .url("https://yh6f0r4529hb.statuspage.io/api/v2/components.json")
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return null;
            }

            return new JSONObject(response.body().string()).getJSONArray("components");

        } catch (final IOException e) {
            return null;
        }
    }

    String toEmoji(String componentStatus) {
        switch (componentStatus.toLowerCase()) {
            case "operational" -> {
                return "<:status_online:774743921560649810>";
            }

            case "degraded_performance" -> {
                return "<:status_idle:774743953303535636>";
            }

            case "partial_outage", "major_outage" -> {
                return "<:status_do_not_disturb:774743995238318112>";
            }

        }
        return "";
    }
}

