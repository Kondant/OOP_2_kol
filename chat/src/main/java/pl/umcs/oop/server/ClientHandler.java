package pl.umcs.oop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final List<ClientHandler> clients;
    private PrintWriter out;
    private String login;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    private void broadcast(String message) {
        for (ClientHandler c : clients) {
            if (c.login != null) {
                c.sendMessage(message);
            }
        }
    }

    private void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    private void sendOnlineUsers() {
        StringBuilder response = new StringBuilder("Użytkownicy online:\n");
        for (ClientHandler c : clients) {
            if (c.login != null) {
                response.append("- ").append(c.login).append("\n");
            }
        }
        sendMessage(response.toString());
    }
    private void handlePrivateMessage(String message) {
        String[] parts = message.split(" ", 3);
        if (parts.length < 3) {
            sendMessage("Użycie: /w <login> <wiadomość>");
            return;
        }

        String recipientLogin = parts[1];
        String privateMsg = parts[2];

        ClientHandler recipient = null;
        for (ClientHandler c : clients) {
            if (recipientLogin.equals(c.login)) {
                recipient = c;
                break;
            }
        }

        if (recipient != null) {
            recipient.sendMessage("[Prywatna od " + this.login + "]: " + privateMsg);
            sendMessage("[Prywatna do " + recipient.login + "]: " + privateMsg);
        } else {
            sendMessage("Użytkownik '" + recipientLogin + "' nie jest zalogowany.");
        }
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out = new PrintWriter(socket.getOutputStream(), true);
            String firstMessage = in.readLine();
            if (firstMessage != null && firstMessage.startsWith("LOGIN:")) {
                this.login = firstMessage.substring("LOGIN:".length());
                broadcast("Użytkownik " + login + " dołączył do czatu.");
                System.out.println("Nowy użytkownik: " + login);
            } else {
                out.println("Błąd: brak loginu. Rozłączam.");
                socket.close();
                return;
            }

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Otrzymano wiadomość od " + login + ": " + message);

                if (message.startsWith("/")) {
                    if (message.equals("/online")) {
                        sendOnlineUsers();
                    } else if (message.startsWith("/w ")) {
                        handlePrivateMessage(message);
                    } else {
                        sendMessage("Nieznana komenda: " + message);
                    }
                } else {
                    broadcast(this.login + ": " + message);
                }
            }

        } catch (IOException e) {
            System.err.println("Błąd komunikacji z " + login + ": " + e.getMessage());
        } finally {
            clients.remove(this);
            broadcast("Użytkownik " + login + " opuścił czat.");
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
