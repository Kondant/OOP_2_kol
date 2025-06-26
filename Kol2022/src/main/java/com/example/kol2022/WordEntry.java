package com.example.kol2022;

import java.time.LocalTime;

public class WordEntry {
    private final LocalTime time;
    private final String word;

    public WordEntry(LocalTime time, String word) {
        this.time = time;
        this.word = word;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getWord() {
        return word;
    }
}
