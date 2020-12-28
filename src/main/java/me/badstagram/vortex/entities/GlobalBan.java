package me.badstagram.vortex.entities;

public class GlobalBan {
    final String banId, userId, proof, reason, moderatorId, approvedBy;

    public GlobalBan(String banId, String userId, String proof, String reason, String moderatorId,
            String approvedBy) {

        this.banId = banId;
        this.userId = userId;
        this.proof = proof;
        this.reason = reason;
        this.moderatorId = moderatorId;
        this.approvedBy = approvedBy;
    }

    public String getUserId() {
        return userId;
    }

    public String getProof() {
        return proof;
    }

    public String getReason() {
        return reason;
    }

    public String getModeratorId() {
        return moderatorId;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public String getBanId() {
        return banId;
    }
}
