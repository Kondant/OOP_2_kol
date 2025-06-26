package com.example.gra;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends GraphicsItem {
    private static int gridRows;
    private static int gridCols;

    private final int gridX;
    private final int gridY;
    private final Color color;

    public static void setGridSize(int rows, int cols) {
        gridRows = rows;
        gridCols = cols;
    }

    public Brick(int gridX, int gridY, Color color) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.color = color;
        this.width = 1.0 / gridCols;
        this.height = 1.0 / gridRows;
        this.x = gridX * width;
        this.y = gridY * height;

    }


    @Override
    public void draw(GraphicsContext gc) {
        double px = x * canvasWidth;
        double py = y * canvasHeight;
        double pwidth = width * canvasWidth;
        double pheight = height * canvasHeight;
        gc.setFill(color);
        gc.fillRect(px, py, pwidth, pheight);
        gc.setStroke(color.brighter());
        gc.setLineWidth(2);
        gc.strokeLine(px, py, px + pwidth, py);
        gc.strokeLine(px, py, px, py + pheight);
        gc.setStroke(color.darker());
        gc.strokeLine(px, py + pheight, px + pwidth, py + pheight);
        gc.strokeLine(px + pwidth, py, px + pwidth, py + pheight);
    }
    public enum CrushType {
        NoCrush,
        HorizontalCrush,
        VerticalCrush
    }
    public CrushType checkCrush(Point2D ballTop, Point2D ballBottom, Point2D ballLeft, Point2D ballRight) {
        double brickLeft = x;
        double brickRight = x + width;
        double brickTop = y;
        double brickBottom = y + height;

        boolean crushHorizontal = false;
        boolean crushVertical = false;
        if ((ballRight.getX() >= brickLeft && ballRight.getX() <= brickRight) &&
                (ballRight.getY() >= brickTop && ballRight.getY() <= brickBottom)) {
            crushHorizontal = true;
        }
        if ((ballLeft.getX() <= brickRight && ballLeft.getX() >= brickLeft) &&
                (ballLeft.getY() >= brickTop && ballLeft.getY() <= brickBottom)) {
            crushHorizontal = true;
        }
        if ((ballTop.getY() <= brickBottom && ballTop.getY() >= brickTop) &&
                (ballTop.getX() >= brickLeft && ballTop.getX() <= brickRight)) {
            crushVertical = true;
        }
        if ((ballBottom.getY() >= brickTop && ballBottom.getY() <= brickBottom) &&
                (ballBottom.getX() >= brickLeft && ballBottom.getX() <= brickRight)) {
            crushVertical = true;
        }
        if (crushHorizontal && crushVertical) {
            return CrushType.VerticalCrush;
        } else if (crushHorizontal) {
            return CrushType.HorizontalCrush;
        } else if (crushVertical) {
            return CrushType.VerticalCrush;
        } else {
            return CrushType.NoCrush;
        }
    }
}
