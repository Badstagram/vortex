package me.badstagram.vortex.entities;

import javax.annotation.Nullable;

public class GuildPunishment {
    public int getCaseId() {
        return caseId;
    }

    private final int caseId;
    private final String userId;
    private final String guildId;
    private final String modId;
    private final String reason;
    private final GuildPunishmentType type;
    private final String length;


    public GuildPunishment(String userId, String guildId, String modId, String reason, @Nullable String length,
            GuildPunishmentType type, int caseId) {
        this.userId = userId;
        this.guildId = guildId;
        this.modId = modId;
        this.reason = reason;
        this.length = length == null ? "Permanent" : length;
        this.type = type;
        this.caseId = caseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getModId() {
        return modId;
    }

    public String getReason() {
        return reason;
    }

    public String getLength() {
        return length;
    }

    public GuildPunishmentType getType() {
        return type;
    }
}
