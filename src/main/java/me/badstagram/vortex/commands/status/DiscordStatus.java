package me.badstagram.vortex.commands.status;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;

public class DiscordStatus extends Command {
    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var components = this.getDiscordStatus();
        var latestIncident = this.getLatestIncident(ctx.getEvent());

        if (components == null || latestIncident == null) {
            ctx.reply("Something went wrong while fetching discord's status. Try again later.");
            return;
        }
        var format = "%s **%s**: %s\n";
        var sb = new StringBuilder();

        for (int i = 0; i < components.length(); i++) {
            JSONObject component = components.getJSONObject(i);
            var componentName = component.getString("name");
            var componentStatus = component.getString("status");
            var emoji = this.toEmoji(componentStatus);

            sb.append(String.format(format, emoji, componentName, FormatUtil.capitlise(componentStatus)));
        }

        sb.append("\nLatest Incident\n")
                .append(MarkdownUtil.maskedLink(latestIncident.getString("name"), latestIncident.getString("shortlink")))
                .append("\n")
                .append("Status: ")
                .append(FormatUtil.capitlise(latestIncident.getString("status")));



        var embed = EmbedUtil.createDefault()
                .setTitle("Discord's Status", "https://discordstatus.com")
                .setColor(ctx.getSelfMember().getColor())
                .setDescription(sb.toString())
                .build();

        ctx.getChannel().sendMessage(embed).queue();

    }

    @Nullable
    JSONArray getDiscordStatus() {
        try {
            OkHttpClient client = Vortex.getHttpClient();

            Request request = new Request.Builder()
                    .url("https://srhpyqt94yxb.statuspage.io/api/v2/components.json")
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

    @Nullable
    JSONObject getLatestIncident(final GuildMessageReceivedEvent event) {
        try {
            OkHttpClient client = Vortex.getHttpClient();

            Request request = new Request.Builder()
                    .url("https://srhpyqt94yxb.statuspage.io/api/v2/incidents.json")
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return null;
            }

            return new JSONObject(response.body().string()).getJSONArray("incidents").getJSONObject(0);

        } catch (final IOException e) {
            return null;
        }

    }

}
