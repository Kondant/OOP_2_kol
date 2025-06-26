package com.example.gra;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle extends GraphicsItem{
    public Paddle() {
        this.width=0.2;
        this.height=0.03;
        this.x=0.4;
        this.y=0.95-this.height;
    }
    @Override
    public void draw(GraphicsContext gc){
        double px=x*canvasWidth;
        double py=y*canvasHeight;
        double pwidth=width*canvasWidth;
        double pheight=height*canvasHeight;
        gc.setFill(Color.BLUE);
        gc.fillRect(px,py,pwidth,pheight);
    }
    public void setCenterX(double centerX){
        this.x=centerX-width/2;
    }
}
