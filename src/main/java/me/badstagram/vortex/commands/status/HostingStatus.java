package me.badstagram.vortex.commands.status;

import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.CommandContext;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;

public class HostingStatus extends Command {
    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        var status = this.getDigitalOceanStatus(ctx.getEvent());

        if (status == null) {
            ctx.reply("Something went wrong while fetching GalaxyGate's status. Try again later.");
            return;
        }

        var format = "%s **%s**: %s\n";
        var sb = new StringBuilder();

        for (int i = 0; i < status.length(); i++) {

            var service = status.getJSONObject(i);

            var serviceName = service.getString("name");
            var serviceStatus = this.formatIncidentName(service.getString("status"));
            var emote = this.toEmoji(serviceStatus);

            sb.append(String.format(format, emote, serviceName, serviceStatus));
        }

        var embed = EmbedUtil.createDefault()
                .setTitle("DigitalOcean's Status", "https://status.digitalocean.com/")
                .setColor(ctx.getSelfMember().getColor())
                .setDescription(sb.toString())
                .build();

        ctx.getChannel().sendMessage(embed).queue();
    }

    @Nullable
    JSONArray getDigitalOceanStatus(GuildMessageReceivedEvent event) {
        try {
            OkHttpClient client = Vortex.getHttpClient();

            Request request = new Request.Builder()
                    .url(new URL("https://s2k7tnzlhrpw.statuspage.io/api/v2/components.json"))
                    .header("Accept", "application/json")
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

    String toEmoji(String serviceStatus) {

        switch (serviceStatus.toLowerCase()) {
            case "operational" -> {
                return "<:online:774743921560649810>";
            }

            case "minor incident" -> {
                return "<:774743953303535636>";
            }

            case "major incident" -> {
                return "<:do_not_disturb:774743995238318112>";
            }

            case "scheduled" -> {
                return "\uD83D\uDD50";
            }

        }
        return "";
    }

    String formatIncidentName(String serviceIncidentName) {
        switch (serviceIncidentName.toLowerCase()) {
            case "operational" -> {
                return "Operational";
            }

            case "degraded_performance" -> {
                return "Degraded Performance";
            }


            case "partial_outage" -> {
                return "Partial Outage";
            }

            case "major_outage" -> {
                return "Major Outage";
            }

        }
        return "";
    }

}
