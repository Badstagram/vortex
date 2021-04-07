package me.badstagram.vortex.entities.enums;

public enum GuildVerificationLevel {
    NONE("NONE","None"),
    LOW("LOW","Low"),
    MEDIUM("MEDIUM","Medium"),
    HIGH("HIGH","High"),
    VERY_HIGH("VERY_HIGH","Very High"),
    UNKNOWN("UNKNOWN", "Unknown");


    private final String apiName, humanName;
    GuildVerificationLevel(String apiName, String humanName) {
        this.apiName = apiName;
        this.humanName = humanName;

    }

    public String getApiName() {
        return apiName;
    }

    public String getHumanName() {
        return humanName;
    }

    public static GuildVerificationLevel fromApiName(String apiName) {
        for (var level : values()) {


            if (level.getApiName().equalsIgnoreCase(apiName))
                return level;
        }

        return UNKNOWN;
    }
}
