package me.badstagram.vortex.entities.enums;

public enum USEPAIndex {
    GOOD(1, "Good"),
    MODERATE(2, "Moderate"),
    UNHEALTHY_FOR_SENSITIVE_GROUP(3, "Unhealthy for sensitive groups"),
    UNHEALTHY(4, "Unhealthy"),
    VERY_UNHEALTHY(5, "Very Unhealthy"),
    HAZARDOUS(6, "Hazardous"),
    UNKNOWN(-1, "");

    private final int index;
    private final String band;

    USEPAIndex(int index, String band) {
        this.index = index;
        this.band = band;
    }

    public String getBand() {
        return band;
    }

    public int getIndex() {
        return index;
    }

    public static USEPAIndex fromIndex(int index) {
        for (var value : values()) {
            if (value.getIndex() == index)
                return value;
        }

        return UNKNOWN;
    }
}
