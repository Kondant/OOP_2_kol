package com.example.kol2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerClient {
    private Socket socket;
    private BufferedReader reader;
    private final Consumer<String> onWordReceived;

    public ServerClient(String host, int port, Consumer<String> onWordReceived) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.onWordReceived = onWordReceived;

        startListening();
    }

    private void startListening() {
        new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                  System.out.println("Odebrano: "+line);
                    onWordReceived.accept(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}