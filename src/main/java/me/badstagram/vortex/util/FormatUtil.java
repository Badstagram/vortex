package me.badstagram.vortex.util;

import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {
    /**
     * Formats seconds into a human readable time
     * Code adapted from https://github.com/jagrosh/Vortex/blob/7dfaf21b33ce0302e485ce61e0d4b8572a2c4cfe/src/main/java/com/jagrosh/vortex/utils/FormatUtil.java#L211
     *
     * @param timeSeconds The time in seconds
     * @return The human readable time
     * @author John Grosh (jagrosh)
     */
    public static String secondsToTimeCompact(long timeSeconds) {
        StringBuilder builder = new StringBuilder();
        int years = (int) (timeSeconds / (60 * 60 * 24 * 365));
        if (years > 0) {
            builder.append(years)
                    .append("y ");
            timeSeconds = timeSeconds % (60 * 60 * 24 * 365);
        }
        int weeks = (int) (timeSeconds / (60 * 60 * 24 * 7));
        if (weeks > 0) {
            builder.append(weeks)
                    .append("w ");

            timeSeconds = timeSeconds % (60 * 60 * 24 * 7);
        }
        int days = (int) (timeSeconds / (60 * 60 * 24));
        if (days > 0) {
            builder.append(days)
                    .append("d ");
            timeSeconds = timeSeconds % (60 * 60 * 24);
        }
        int hours = (int) (timeSeconds / (60 * 60));
        if (hours > 0) {
            builder.append(hours)
                    .append("h ");
            timeSeconds = timeSeconds % (60 * 60);
        }
        int minutes = (int) (timeSeconds / (60));
        if (minutes > 0) {
            builder.append(minutes)
                    .append("m ");
            timeSeconds = timeSeconds % (60);
        }
        if (timeSeconds > 0)
            builder.append(timeSeconds)
                    .append("s ");
        String str = builder.toString();
        if (str.endsWith(", "))
            str = str.substring(0, str.length() - 2);
        if (str.isEmpty())
            str = "No time";
        return str;
    }

    public static String capitlise(@Nonnull String text) {
        Checks.notNull(text, "Text");

        StringBuilder s = new StringBuilder();

        // Declare a character of space
        // To identify that the next character is the starting
        // of a new word
        char ch = ' ';
        for (int i = 0; i < text.length(); i++) {

            // If previous character is space and current
            // character is not space then it shows that
            // current letter is the starting of the word
            if (ch == ' ' && text.charAt(i) != ' ')
                s.append(Character.toUpperCase(text.charAt(i)));
            else
                s.append(text.charAt(i));
            ch = text.charAt(i);
        }

        // Return the string with trimming
        return s.toString()
                .trim();
    }

    public static String secondsToTime(long timeSeconds) {
        StringBuilder builder = new StringBuilder();
        int years = (int) (timeSeconds / (60 * 60 * 24 * 365));
        if (years > 0) {
            builder.append(years)
                    .append(" years ");
            timeSeconds = timeSeconds % (60 * 60 * 24 * 365);
        }

        int months = (int) (timeSeconds / (60 * 60 * 24 * 30));
        if (months > 0) {
            builder.append(months)
                    .append(" months ");
            timeSeconds = timeSeconds % (60 * 60 * 24 * 30);
        }

        int weeks = (int) (timeSeconds / (60 * 60 * 24 * 7));
        if (weeks > 0) {
            builder.append(weeks)
                    .append(" weeks ");

            timeSeconds = timeSeconds % (60 * 60 * 24 * 7);
        }

        int days = (int) (timeSeconds / (60 * 60 * 24));
        if (days > 0) {
            builder.append(days)
                    .append(" days ");
            timeSeconds = timeSeconds % (60 * 60 * 24);
        }

        int hours = (int) (timeSeconds / (60 * 60));
        if (hours > 0) {
            builder.append(hours)
                    .append(" hours ");
            timeSeconds = timeSeconds % (60 * 60);
        }

        int minutes = (int) (timeSeconds / (60));
        if (minutes > 0) {
            builder.append(minutes)
                    .append(" minutes ");
            timeSeconds = timeSeconds % (60);
        }

        if (timeSeconds > 0)
            builder.append(timeSeconds)
                    .append(" seconds");

        String str = builder.toString();
        if (str.endsWith(", "))
            str = str.substring(0, str.length() - 2);

        if (str.isEmpty())
            str = "No time";
        return str;
    }

    public static String formatDate(Instant time) {
        var dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault());

        return dtf.format(time);
    }

    public static String formatDate(OffsetDateTime time) {
        return formatDate(time.toInstant());
    }

    public static String formatDate(long epochMilis) {
        return formatDate(Instant.ofEpochMilli(epochMilis));
    }

    public static String parseGuildFeature(String feature) {
        return capitlise(feature.toLowerCase()
                .replaceAll("_", " "));

    }


}
