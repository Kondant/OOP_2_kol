package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.DrawingServer;
import app.Segment;

import java.util.List;

public class DrawingApp extends Application {
    private int offsetX = 0;
    private int offsetY = 0;
    private Canvas canvas;
    private DrawingServer server;

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        server = new DrawingServer(gc, 12345); // port np. 12345
        server.start();

        BorderPane root = new BorderPane(canvas);
        Scene scene = new Scene(root);

        // Obsługa klawiatury
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) offsetY += 10;
            if (e.getCode() == KeyCode.DOWN) offsetY -= 10;
            if (e.getCode() == KeyCode.LEFT) offsetX += 10;
            if (e.getCode() == KeyCode.RIGHT) offsetX -= 10;

            redraw();
            stage.setTitle("Przesunięcie: (" + offsetX + ", " + offsetY + ")");
        });

        stage.setTitle("Przesunięcie: (0, 0)");
        stage.setScene(scene);
        stage.show();
    }

    private void redraw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        List<Segment> segments = server.getSegments();
        synchronized (segments) {
            for (Segment s : segments) {
                gc.setStroke(s.color);
                gc.strokeLine(
                        s.x1 + offsetX,
                        s.y1 + offsetY,
                        s.x2 + offsetX,
                        s.y2 + offsetY
                );
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
