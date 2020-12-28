package me.badstagram.vortex.util;

public class Convert {
    private Convert() {}

    public static long toDays(int howMany) {
        return howMany * 60 * 60 * 24;
    }

    public static long toHours(int howMany) {
        return howMany * 60 * 60;
    }

    public static long toMinutes(int howMany) {
        return howMany * 60;
    }
}
