package me.badstagram.vortex.util;

import me.badstagram.vortex.core.Colors;
import me.badstagram.vortex.entities.enums.GuildPunishmentType;
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

    public static EmbedBuilder createDefaultWarning() {
        return new EmbedBuilder()
                .setTimestamp(OffsetDateTime.now())
                .setColor(Colors.WARN.getAsColor());
    }

    public static EmbedBuilder createDefaultPunishment(String type) {
        return createDefaultError()
                .setTitle("Punishment | " + type);

    }

    public static EmbedBuilder createDefaultPunishment(GuildPunishmentType type) {
        return createDefaultError()
                .setTitle("Punishment | " + type.getName());

    }



}
