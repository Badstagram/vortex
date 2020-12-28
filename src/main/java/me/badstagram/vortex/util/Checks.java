package me.badstagram.vortex.util;

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
}
