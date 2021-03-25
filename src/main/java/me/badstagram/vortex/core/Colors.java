package me.badstagram.vortex.core;

import java.awt.*;

public enum Colors {
    SUCCESS(86, 255, 86),
    WARN(255, 255, 0),
    ERROR(255, 64, 64),
    RANDOM(Vortex.getRandom()
            .nextInt(255), Vortex.getRandom()
            .nextInt(255), Vortex.getRandom()
            .nextInt(255));

    private final int r;
    private final int g;
    private final int b;
    private final Color asColor;

    Colors(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.asColor = new Color(r, g, b);
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
        return this.asColor;
    }
}
