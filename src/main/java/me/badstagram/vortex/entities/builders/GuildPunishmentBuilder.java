package me.badstagram.vortex.entities.builders;

import me.badstagram.vortex.entities.GuildPunishment;
import me.badstagram.vortex.entities.GuildPunishmentType;

public class GuildPunishmentBuilder {
    private String userId;
    private String guildId;
    private String modId;
    private String reason;
    private String length = "Permanent";
    private GuildPunishmentType type;
    private int caseId;



    public GuildPunishmentBuilder setType(GuildPunishmentType type) {
        this.type = type;
        return this;
    }

    public GuildPunishmentBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public GuildPunishmentBuilder setGuildId(String guildId) {
        this.guildId = guildId;
        return this;
    }

    public GuildPunishmentBuilder setModId(String modId) {
        this.modId = modId;
        return this;
    }

    public GuildPunishmentBuilder setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public GuildPunishmentBuilder setLength(String length) {
        this.length = length;
        return this;
    }

    public GuildPunishmentBuilder setCaseId(int caseId) {
        this.caseId = caseId;
        return this;
    }

    public GuildPunishment build() {
        return new GuildPunishment(this.userId, this.guildId, this.modId, this.reason, this.length, this.type, this.caseId);
    }
}
