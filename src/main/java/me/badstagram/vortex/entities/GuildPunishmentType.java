package me.badstagram.vortex.entities;

public enum GuildPunishmentType {
    TEMPMUTE("Temp Mute"),
    TEMPBAN("Temp Ban"),
    MUTE("Mute"),
    BAN("Ban"),
    SOFTBAN("Soft Ban"),
    KICK("Kick"),
    WARN("Warn"),
    UNKNOWN("Unknown");

    private final String name;


    GuildPunishmentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static GuildPunishmentType fromName(String name) {
        for (var type : values()) {
            if (type.name.equalsIgnoreCase(name)) return type;
        }

        return UNKNOWN;
    }
}
