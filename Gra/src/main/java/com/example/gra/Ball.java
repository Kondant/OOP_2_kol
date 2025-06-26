package com.example.gra;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends GraphicsItem{
    private Point2D moveVector;
    private double velocity;
    public Ball(){
        this.width=0.03;
        this.height=0.03;
        this.x=0.5-width/2;
        this.y=0.5-height/2;
    this.velocity=0.3;
    double angleRadians=Math.toRadians(45);
    double dx=Math.cos(angleRadians);
    double dy=-Math.sin(angleRadians);
    this.moveVector=new Point2D(dx,dy);
    }
public void bounceHorizontally(){
        moveVector=new Point2D(-moveVector.getX(),moveVector.getY());
}
    public void bounceVertically() {
        moveVector = new Point2D(moveVector.getX(), -moveVector.getY());
    }
    @Override
    public void draw(GraphicsContext gc){
        double px=x*canvasWidth;
        double py=y*canvasHeight;
        double pwidth=width*canvasWidth;
        double pheight=height*canvasHeight;
        gc.setFill(Color.WHITE);
        gc.fillOval(px,py,pwidth,pheight);
    }
    public void setPosition(Point2D point) {
        this.x = point.getX() - width / 2;
        this.y = point.getY() - height / 2;

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + width > 1) x = 1 - width;
        if (y + height > 1) y = 1 - height;
    }
public void updatePosition(double deltaSeconds){
        this.x +=moveVector.getX()*velocity*deltaSeconds;
        this.y +=moveVector.getY()*velocity*deltaSeconds;
}
    public Point2D getTop() {
        return new Point2D(x + width / 2, y);
    }

    public Point2D getBottom() {
        return new Point2D(x + width / 2, y + height);
    }

    public Point2D getLeft() {
        return new Point2D(x, y + height / 2);
    }

    public Point2D getRight() {
        return new Point2D(x + width, y + height / 2);
    }
    public void bounceFromPaddle(double hitPosition) {
        double maxBounceAngle = Math.toRadians(75);
        double bounceAngle = hitPosition * maxBounceAngle;
        double speed = velocity;
        double dx = Math.sin(bounceAngle);
        double dy = -Math.cos(bounceAngle);
        this.moveVector = new Point2D(dx, dy);
    }

}


