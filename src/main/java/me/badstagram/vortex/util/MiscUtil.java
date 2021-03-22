package me.badstagram.vortex.util;

import me.badstagram.vortex.core.Vortex;
import me.badstagram.vortex.entities.enums.GuildPunishmentType;
import me.badstagram.vortex.managers.GuildSettingsManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.utils.data.DataObject;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URL;
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

        var response = client.newCall(request).execute();

        if (response.body() == null || !response.isSuccessful()) return "null";

        var data = DataObject.fromJson(response.body().string());

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
                .appendYears().appendSuffix("y")
                .appendMonths().appendSuffix("mo")
                .appendDays().appendSuffix("d")
                .appendHours().appendSuffix("h")
                .appendMinutes().appendSuffix("m")
                .appendSeconds().appendSuffix("s")
                .toFormatter();

        return formatter.parsePeriod(timeString);
    }

    public static void logPunishment(Guild guild, GuildPunishmentType type, @Nullable String length) {
        var settingsManager = new GuildSettingsManager(guild);

        var punishLogId = settingsManager.getPunishLogChannel();

        if (punishLogId == null)
            return;



    }
}
