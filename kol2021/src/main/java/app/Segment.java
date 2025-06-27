package app;

import javafx.scene.paint.Color;

public class Segment {
    public final double x1, y1, x2, y2;
    public final Color color;

    public Segment(double x1, double y1, double x2, double y2, Color color) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
        this.color = color;
    }
}

