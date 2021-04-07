package me.badstagram.vortex.commands.info;

import me.badstagram.vortex.commandhandler.Category;
import me.badstagram.vortex.commandhandler.Command;
import me.badstagram.vortex.commandhandler.context.impl.CommandContext;
import me.badstagram.vortex.core.Config;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.exceptions.BadArgumentException;
import me.badstagram.vortex.exceptions.CommandExecutionException;
import me.badstagram.vortex.util.EmbedUtil;
import me.badstagram.vortex.util.FormatUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Rep extends Command {
    public Rep() {
        this.name = "rep";
        this.help = "Get the reputation of a user from Discordrep.com";
        this.usage = "rep [user]";
        this.botPermissions = new Permission[] {Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("Info");


    }

    @Override
    public void execute(CommandContext ctx) throws CommandExecutionException, BadArgumentException {
        try {
            var formatter = DateTimeFormatter.ofPattern("dd:MM:yyyy hh:mm")
                    .withZone(ZoneId.of("GMT+0000"));
//                    .withLocale(Locale.ENGLISH);

            var parsedUsers = ctx.createArgumentParser()
                    .parseUser();

            var user = parsedUsers.isEmpty() ? ctx.getAuthor() : parsedUsers.get(0);


            var rep = this.getRep(user.getId());
            var infractions = this.getInfractions(user.getId());

            // rep
            var staff = rep.getBoolean("staff");
            var upvotes = rep.getInt("upvotes");
            var xp = rep.getInt("xp");
            var downvotes = rep.getInt("downvotes");
            var rank = rep.getString("rank");

            // infractions
            var type = FormatUtil.capitlise(infractions.getString("type"));
            var reason = infractions.getString("reason", "");
            var moderator = infractions.getString("moderator", "");
            var date = infractions.getInt("date", 0);

            ctx.getJDA()
                    .retrieveUserById(moderator)
                    .queue(mod -> {
                        var description = """
                                **Rep**
                                                    
                                **Staff**: %s
                                **Upvotes**: %d
                                **Downvotes**: %d
                                **Rank**: %s
                                **XP**: %d
                                                    
                                **Infractions**
                                                    
                                **Type**: %s
                                **Reason**: %s
                                **Moderator**: %s
                                **Date**: %s
                                                    
                                """
                                .formatted(staff ? "Yes" : "No", upvotes, downvotes, rank, xp, type, reason, mod.getAsTag(),
                                        date == 0L ? "" : formatter.format(Instant.ofEpochMilli(Long.parseLong(String.valueOf(date)))));

                        var embed = EmbedUtil.createDefault()
                                .setAuthor(user.getAsTag(), user.getEffectiveAvatarUrl())
                                .setDescription(description)
                                .build();

                        ctx.getChannel()
                                .sendMessage(embed)
                                .queue();
                    });


        } catch (Exception e) {
            throw new CommandExecutionException(e);
        }
    }

    protected DataObject getRep(String userId) throws IOException {
        var client = Vortex.getHttpClient();

        var url = new HttpUrl.Builder()
                .scheme("https")
                .host("discordrep.com")
                .addPathSegments("api/v3/rep")
                .addPathSegment(userId)
                .build();

        var request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Config.get("discord_rep_token"))
                .build();

        var response = client.newCall(request)
                .execute();

        if (!response.isSuccessful() || response.body() == null) {
            return null;
        }

        return DataObject.fromJson(response.body().string());
    }

    protected DataObject getInfractions(String userId) throws IOException {
        var client = Vortex.getHttpClient();

        var url = new HttpUrl.Builder()
                .scheme("https")
                .host("discordrep.com")
                .addPathSegments("api/v3/infractions")
                .addPathSegment(userId)
                .build();

        var request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Config.get("discord_rep_token"))
                .build();

        var response = client.newCall(request)
                .execute();

        if (!response.isSuccessful() || response.body() == null) {
            return null;
        }

        return DataObject.fromJson(response.body().string());
    }
}
