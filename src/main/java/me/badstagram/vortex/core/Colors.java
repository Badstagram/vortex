package me.badstagram.vortex.core;

import java.awt.*;

public enum Colors {
    SUCCESS(86, 255, 86),
    WARN(255, 255, 0),
    ERROR(255, 64, 64);

    private final int r;
    private final int g;
    private final int b;

    Colors(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public Color getAsColor() {
        return new Color(this.r, this.g, this.b);
    }
}
