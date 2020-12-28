package me.badstagram.vortex.automod;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum AutoModPunishmentType {
    IGNORE("Ignore"),
    WARN("Warn"),
    BAN("Ban"),
    UNKNOWN("Unknown");


    private final String name;

    AutoModPunishmentType(String name) {
        this.name = name;
    }

    public static AutoModPunishmentType fromName(String name) {

        var exists = Arrays.stream(values())
                .anyMatch(val -> val.getName().equals(name));

        return exists ? Arrays.stream(values())
                .filter(val -> val.getName().equals(name))
                .collect(Collectors.toList())
                .get(0) : UNKNOWN;
    }

    public String getName() {
        return this.name;
    }
}
