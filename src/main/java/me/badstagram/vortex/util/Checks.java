package me.badstagram.vortex.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.Objects;

public class Checks {
    public static boolean isValidInt(Object o) {
        if (o instanceof Integer) return true;
        if (!(o instanceof String)) return false;

        try {
            Integer.parseInt((String) o);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static boolean canEmbedLinks(TextChannel tc, Member member) {
        return member.hasPermission(tc, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS);
    }

    public static boolean noneNull(Object... objects) {

        return Arrays.stream(objects)
                .noneMatch(Objects::isNull);
    }
}
