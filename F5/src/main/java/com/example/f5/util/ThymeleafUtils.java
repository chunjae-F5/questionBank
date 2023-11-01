package com.example.f5.util;

public class ThymeleafUtils {
    private int unitCounter;

    public ThymeleafUtils() {
        this.unitCounter = 0;
    }

    public String getNextId(int chapterIndex) {
        unitCounter++;
        return String.format("chk%02d_%02d", chapterIndex, unitCounter);
    }

    public String getId(int chapterIndex) {
        return String.format("chk%02d_%02d", chapterIndex, unitCounter);
    }

    public void reset() {
        this.unitCounter = 0;
    }
}
