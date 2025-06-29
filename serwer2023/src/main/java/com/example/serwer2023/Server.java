package com.example.serwer2023;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 5000;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Serwer uruchomiony na porcie " + port);

            // Uruchomienie GUI
            FilterGUI gui = new FilterGUI();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Połączono z klientem.");

                new ClientHandler(clientSocket, gui).run(); // sekwencyjnie
                System.out.println("Zakończono obsługę klienta.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
