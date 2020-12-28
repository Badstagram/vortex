package me.badstagram.vortex.entities.builders;

import me.badstagram.vortex.entities.GlobalBan;

public class GlobalBanBuilder {

    String banId, userId, proof, reason, moderatorId, approvedBy;

    public GlobalBanBuilder setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public GlobalBanBuilder setProof(String proof) {
        this.proof = proof;
        return this;
    }

    public GlobalBanBuilder setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public GlobalBanBuilder setModeratorId(String moderatorId) {
        this.moderatorId = moderatorId;
        return this;
    }

    public GlobalBanBuilder setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
        return this;
    }

    public GlobalBanBuilder setBanId(String banId) {
        this.banId = banId;
        return this;
    }

    public GlobalBan build() {
        return new GlobalBan(banId, this.userId, this.proof, this.reason, this.moderatorId, this.approvedBy);
    }
}
