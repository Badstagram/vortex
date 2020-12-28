package me.badstagram.vortex.core;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.internal.utils.Checks;

public class Config {
    public static String get(String key) {
        Checks.notNull(key, "Key");

        var env = Dotenv.load();
        return env.get(key.toUpperCase());
    }
}
