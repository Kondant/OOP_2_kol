package com.example.gra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GameCanvas canvas= new GameCanvas(800,600);
        canvas.draw();
        Pane root=new Pane(canvas);
        Scene scene=new Scene(root);
        stage.setTitle("Gra");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}