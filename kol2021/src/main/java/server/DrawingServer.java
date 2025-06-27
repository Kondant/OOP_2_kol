package server;

import app.Segment;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawingServer {
    private final GraphicsContext graphicsContext;
    private final int port;
    private final List<Segment> segments = Collections.synchronizedList(new ArrayList<>());

    public DrawingServer(GraphicsContext graphicsContext, int port) {
        this.graphicsContext = graphicsContext;
        this.port = port;
    }

    public void start() {
        Thread serverthread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Serwer nasluchuje na porcie " + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverthread.setDaemon(true);
        serverthread.start();
    }

    private Color[] currentColor = {Color.BLACK};

    private void handleClient(Socket clientSocket) {
        Thread clientThread = new Thread(() -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    String message = line.trim();
                    if (message.length() == 6) {
                        String hex = "#" + message;

                        Color newColor = Color.web(hex);
                        Platform.runLater(() -> currentColor[0] = newColor);
                    } else {
                        String[] parts = message.split(" ");
                        double x1 = Double.parseDouble(parts[0]);
                        double y1 = Double.parseDouble(parts[1]);
                        double x2 = Double.parseDouble(parts[2]);
                        double y2 = Double.parseDouble(parts[3]);

                        Platform.runLater(() -> {
                            Segment s = new Segment(x1, y1, x2, y2, currentColor[0]);
                            segments.add(s);
                            graphicsContext.setStroke(s.color);
                            graphicsContext.strokeLine(s.x1, s.y1, s.x2, s.y2);
                        });

                    }
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();
    }
    public List<Segment> getSegments() {
        return segments;
    }

}
