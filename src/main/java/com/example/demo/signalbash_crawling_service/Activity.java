package com.example.demo.signalbash_crawling_service;

public record Activity(String dawTime, Integer streak) {
    public Activity(String dawTime, Integer streak) {
        this.dawTime = dawTime;
        this.streak = streak;
    }

    @Override
    public final String toString() {
        return String.format("Activity [dawTime=%s, streak=%d]", dawTime, streak);
    }
}
