package com.example.serwer2023;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final FilterGUI gui;

    public ClientHandler(Socket socket, FilterGUI gui) {
        this.socket = socket;
        this.gui = gui;
    }

    public void run() {
        try (
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
        ) {
            File originalFile = Utils.receiveAndSaveImage(in);

            int filterSize = gui.getCurrentRadius(); // tylko nieparzyste

            long startTime = System.currentTimeMillis();
            BufferedImage blurred = ImageProcessor.applyBoxBlur(originalFile, filterSize);
            long endTime = System.currentTimeMillis();

            File outputFile = Utils.saveImage(blurred);
            DatabaseManager.saveToDatabase(originalFile.getPath(), filterSize, (int)(endTime - startTime));

            Utils.sendImage(out, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
