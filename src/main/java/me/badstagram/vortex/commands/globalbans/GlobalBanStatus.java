package me.badstagram.vortex.commands.globalbans;

public enum GlobalBanStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    DENIED("Denied");

    private final String name;

    GlobalBanStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
