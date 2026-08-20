package com.mindbrief.service;

public class SummarizerFactory {
    public static SummarizerStrategy create(String type) {
        return switch (type.toLowerCase()) {
            case "ai" -> new AISummarizer();
            case "extractive", "offline" -> new ExtractiveSummarizer(3);
            default -> throw new IllegalArgumentException("Unknown summarizer type: " + type);
        };
    }
}
