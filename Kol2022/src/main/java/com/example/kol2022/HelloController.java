package com.example.kol2022;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.Socket;
import java.net.URL;
import java.text.Normalizer;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label wordCountLabel;
    private ServerClient serverClient;
    private final List<WordEntry> receivedWords = new ArrayList<>();
    @FXML
    private ListView<String> wordList;
    @FXML
    private TextField filterField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterField.textProperty().addListener((obs, oldValue, newValue) -> {
        updateWordList();
    });
        new Thread(() -> {
            try {
                serverClient = new ServerClient("localhost", 5000,word ->{
                    Platform.runLater(()-> {
                        receivedWords.add(new WordEntry(LocalTime.now(),word));
                        wordCountLabel.setText(String.valueOf(receivedWords.size()));
                        updateWordList();
                    });
                });
                System.out.println("Połączono z serwerem");

            } catch (Exception e) {
                Platform.runLater(() -> wordCountLabel.setText("Błąd połączenia z serwerem"));
                e.printStackTrace();
            }

        }).start();

    }

    private void updateWordList() {
        String filter = filterField.getText().toLowerCase();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<WordEntry> filteredSorted = receivedWords.stream()
                .filter(entry -> {
                    if (filter.isEmpty()) return true;
                    String normalizedWord = normalizeWord(entry.getWord().toLowerCase());
                    return normalizedWord.startsWith(normalizeWord(filter));
                })
                .sorted((e1, e2) -> {
                    String w1 = normalizeWord(e1.getWord().toLowerCase());
                    String w2 = normalizeWord(e2.getWord().toLowerCase());
                    return w1.compareTo(w2);
                })
                .toList();

        List<String> finalList = filteredSorted.stream()
                .map(entry -> entry.getTime().format(formatter) + " " + entry.getWord())
                .toList();

        wordList.getItems().setAll(finalList);
    }

    private String normalizeWord(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }



}