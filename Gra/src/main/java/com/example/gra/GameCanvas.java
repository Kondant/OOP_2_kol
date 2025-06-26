package com.example.gra;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameCanvas extends Canvas {
    private Paddle paddle;
    private Ball ball;
    private final GraphicsContext gc;
    private boolean gameRunning = false;
    private AnimationTimer gameLoop;
    private long lastUpdateTime = 0;
    private List<Brick> bricks = new ArrayList<>();

    public GameCanvas(double width, double height) {
        super(width, height);
        this.gc = getGraphicsContext2D();
        GraphicsItem.setCanvasHeight(height);
        GraphicsItem.setCanvasWidth(width);
        paddle = new Paddle();
        ball = new Ball();
        loadLevel();
        this.setOnMouseMoved(event -> {
            double normalizedX = event.getX() / getWidth();
            paddle.setCenterX(normalizedX);
            if (!gameRunning) {
                double ballCenterX = paddle.getX() + paddle.getWidth() / 2;
                double ballCenterY = paddle.getY() - ball.getHeight() / 2;
                ball.setPosition(new Point2D(ballCenterX, ballCenterY));
            }
            draw();
        });
        this.setOnMouseClicked(event -> {
            if (!gameRunning) {
                gameRunning = true;
                startGameLoop();
            }
        });

        double ballCenterX = paddle.getX() + paddle.getWidth() / 2;
        double ballCenterY = paddle.getY() - ball.getHeight() / 2;
        ball.setPosition(new Point2D(ballCenterX, ballCenterY));

        draw();
    }

    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                    return;
                }
                double deltaSeconds = (now - lastUpdateTime) / 1_000_000_000.0;
                lastUpdateTime = now;

                if (gameRunning) {
                    ball.updatePosition(deltaSeconds);

                    for (int i = 0; i < bricks.size(); i++) {
                        Brick brick = bricks.get(i);
                        Brick.CrushType crush = brick.checkCrush(
                                ball.getTop(),
                                ball.getBottom(),
                                ball.getLeft(),
                                ball.getRight()
                        );
                        if (crush != Brick.CrushType.NoCrush) {

                            if (crush == Brick.CrushType.HorizontalCrush) {
                                ball.bounceHorizontally();
                            } else if (crush == Brick.CrushType.VerticalCrush) {
                                ball.bounceVertically();
                            }
                            bricks.remove(i);
                            i--;
                            break;
                        }
                    }
                    if (shouldBallBounceHorizontally()) {
                        ball.bounceHorizontally();
                    }
                    if (shouldBallBounceFromPaddle()) {
                        double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                        double ballCenter = ball.getX() + ball.getWidth() / 2;
                        double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);
                        ball.bounceFromPaddle(hitPosition);
                    }
                    if (shouldBallBounceVertically()) {
                        ball.bounceVertically();
                    }
                }

                draw();
            }
        };
        gameLoop.start();
    }

    public void loadLevel() {
        Brick.setGridSize(20, 10);
        bricks.clear();
        Color[] rowColors = {
                Color.DARKGREEN,
                Color.DARKRED,
                Color.DARKBLUE,
                Color.GREEN,
                Color.BROWN,
                Color.PURPLE
        };
        for (int row = 0; row < 6; row++) {
            Color color = rowColors[row];
            for (int col = 0; col < 10; col++) {
                bricks.add(new Brick(col, row, color));
            }
        }

    }



    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        for (Brick brick : bricks) {
            brick.draw(gc);
        }

        paddle.draw(gc);
        ball.draw(gc);
    }

    private boolean shouldBallBounceHorizontally() {
        return ball.getX() <= 0 || (ball.getX() + ball.getWidth()) >= 1;
    }

    private boolean shouldBallBounceVertically() {
        return ball.getY() <= 0;
    }

    private boolean shouldBallBounceFromPaddle() {
        double ballRight = ball.getX() + ball.getWidth();
        double ballBottom = ball.getY() + ball.getHeight();

        double paddleLeft = paddle.getX();
        double paddleRight = paddle.getX() + paddle.getWidth();
        double paddleTop = paddle.getY();
        boolean verticallyAligned = ballBottom >= paddleTop && ball.getY() <= paddleTop + paddle.getHeight();
        boolean horizontallyAligned = ballRight >= paddleLeft && ball.getX() <= paddleRight;
        return verticallyAligned && horizontallyAligned;
    }

}
