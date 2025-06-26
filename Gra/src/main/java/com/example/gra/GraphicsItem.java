package com.example.gra;

import javafx.scene.canvas.GraphicsContext;

public abstract class GraphicsItem {
   protected static double canvasWidth,canvasHeight;
    protected double x,y,width,height;
    public static void setCanvasWidth(double canvasWidth) {
        GraphicsItem.canvasWidth = canvasWidth;
    }

    public static void setCanvasHeight(double canvasHeight) {
        GraphicsItem.canvasHeight = canvasHeight;
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
    public abstract void draw(GraphicsContext gc);
}

