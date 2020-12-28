package me.badstagram.vortex.core;

public enum RunMode {
    STABLE("702129848910610452","NzAyMTI5ODQ4OTEwNjEwNDUy", "-", "Stable"),
    NIGHTLY("741474369406107698", "NzQxNDc0MzY5NDA2MTA3Njk4","--", "Nightly"),
    DEVELOPMENT("703651420817195048", "NzAzNjUxNDIwODE3MTk1MDQ4","vd!", "Development"),
    UNKNOWN(null, null, "-","Unknown");

    private final String clientId, prefix, name, base64;

    RunMode(String clientId, String base64, String prefix, String name) {
        this.clientId = clientId;
        this.prefix = prefix;
        this.name = name;
        this.base64 = base64;
    }

    public static RunMode fromId(String id) {
        for (var mode : values()) {
            if (mode.clientId.equals(id)) {
                return mode;
            }
        }
        return UNKNOWN;
    }

    public static RunMode fromBase64(String id) {
        for (var mode : values()) {
            if (mode.base64.equals(id)) {
                return mode;
            }
        }
        return UNKNOWN;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getClientId() {
        return clientId;
    }

    public String getName() {
        return name;
    }
}
