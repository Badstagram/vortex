package me.badstagram.vortex.entities.enums;

public enum GBDefraIndex {
    ONE(1, "Low"),
    TWO(2, "Low"),
    THREE(3, "Low"),
    FOUR(4, "Moderate"),
    FIVE(5, "Moderate"),
    SIX(6, "Moderate"),
    SEVEN(7, "High"),
    EIGHT(8, "High"),
    NINE(9, "High"),
    TEN(10, "Very High"),

    UNKNOWN(-1, "");

    private final int index;
    private final String band;

    GBDefraIndex(int index, String band) {
        this.index = index;
        this.band = band;
    }

    public String getBand() {
        return band;
    }

    public int getIndex() {
        return index;
    }

    public static GBDefraIndex fromIndex(int index) {
        for (var value : values()) {
            if (value.getIndex() == index)
                return value;
        }

        return UNKNOWN;
    }
}
