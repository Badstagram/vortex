package me.badstagram.vortex.util;

import me.badstagram.vortex.core.Config;
import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.entities.enums.GuildPunishmentType;
import me.badstagram.vortex.managers.GuildSettingsManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MiscUtil {
    public static boolean listContainsElement(List<?> list, Object element) {
        return list.contains(element);
    }

    public static String postToHasteBin(String text) throws IOException {

        final String BASE_URL = "https://hastebin.de/";

        var client = Vortex.getHttpClient();
        var url = new URL(BASE_URL + "documents");

        var request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("text/plain"), text))
                .build();

        var response = client.newCall(request)
                .execute();

        var body = response.body();

        if (body == null || !response.isSuccessful()) return "null";

        var data = DataObject.fromJson(body.string());

        return BASE_URL + data.getString("key");
    }

    /**
     * just does nothing
     */
    public static void noop() {
        // just do nothing lol
    }

    public static Period parseTimeString(String timeString) {

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears()
                .appendSuffix("y")
                .appendMonths()
                .appendSuffix("mo")
                .appendDays()
                .appendSuffix("d")
                .appendHours()
                .appendSuffix("h")
                .appendMinutes()
                .appendSuffix("m")
                .appendSeconds()
                .appendSuffix("s")
                .toFormatter();

        return formatter.parsePeriod(timeString);
    }

    public static void logPunishment(Guild guild, GuildPunishmentType type, @Nullable String length) {
        var settingsManager = new GuildSettingsManager(guild);

        var punishLogId = settingsManager.getPunishLogChannel();

        if (punishLogId == null)
            return;
    }


    public static String postToGithubGist(@Nonnull String text) throws IOException {
        var client = Vortex.getHttpClient();

        var req = new Request.Builder()
                .url(new URL("https://api.github.com/gists"))
                .addHeader("Accept", "application/vnd.github.v3+json");

        return "null";
    }

    public static String readInputStream(@Nonnull InputStream stream) {
        var sb = new StringBuilder();
        var line = "";


        try (var streamReader = new BufferedReader(new InputStreamReader(stream))) {
            while ((line = streamReader.readLine()) != null) {
                sb.append(line)
                        .append("\n");
            }
        } catch (IOException e) {
            ErrorHandler.handle(e);
        }
        return sb.toString();
    }

    /**
     * @param flag   The pride flag to overlay onto the avatar
     * @param avatar The users avatar
     * @return a {@link DataObject} containing the data from the api
     */
    public static byte[] getPrideFlag(@Nonnull String flag, @Nonnull String... avatar) {
        try {

            var requestBodyJson = DataObject.empty()
                    .put("images", DataArray.empty()
                            .addAll(Arrays.asList(avatar)))
                    .put("opacity", 64)
                    .toString();
            var req = new Request.Builder()
                    .url("https://api.pxlapi.dev/flag/" + flag)
                    .addHeader("Authorization", "Application " + Config.get("pxlapi"))
                    .post(RequestBody.create(MediaType.parse("application/json"), requestBodyJson))
                    .build();


            var res = Vortex.getHttpClient()
                    .newCall(req)
                    .execute();

            var responseBody = res.body();

            if (responseBody == null || !res.isSuccessful()) {
                Vortex.getLogger()
                        .error("pxlapi returned unknown error: {}", res.code());
                return new byte[0];
            }

            return responseBody.bytes();
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }

        return new byte[0];
    }

}
