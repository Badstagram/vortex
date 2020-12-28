package me.badstagram.vortex.util;

import me.badstagram.vortex.core.Colors;
import net.dv8tion.jda.api.EmbedBuilder;

import java.time.OffsetDateTime;

public class EmbedUtil {

    public static EmbedBuilder createDefault() {
        return new EmbedBuilder()
                .setTimestamp(OffsetDateTime.now())
                .setColor(Colors.SUCCESS.getAsColor());
    }

    public static EmbedBuilder createDefaultError() {
        return new EmbedBuilder()
                .setTimestamp(OffsetDateTime.now())
                .setColor(Colors.ERROR.getAsColor());
    }
}
