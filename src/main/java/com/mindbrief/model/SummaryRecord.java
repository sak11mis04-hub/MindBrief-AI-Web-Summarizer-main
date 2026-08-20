package com.mindbrief.model;

import java.time.LocalDateTime;

public class SummaryRecord {
    private final int id;
    private final String source;
    private final int originalLength;
    private final String summaryText;
    private final String method;
    private final String createdAt;

    private SummaryRecord(Builder builder) {
        this.id = builder.id;
        this.source = builder.source;
        this.originalLength = builder.originalLength;
        this.summaryText = builder.summaryText;
        this.method = builder.method;
        this.createdAt = builder.createdAt;
    }

    public int getId() { return id; }
    public String getSource() { return source; }
    public int getOriginalLength() { return originalLength; }
    public String getSummaryText() { return summaryText; }
    public String getMethod() { return method; }
    public String getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Method: %s | Original length: %d chars%n  Summary: %s",
                createdAt, source, method, originalLength, summaryText);
    }

    public static class Builder {
        private int id;
        private String source = "Unknown";
        private int originalLength;
        private String summaryText = "";
        private String method = "Unknown";
        private String createdAt = LocalDateTime.now().toString();

        public Builder id(int id) { this.id = id; return this; }
        public Builder source(String source) { this.source = source; return this; }
        public Builder originalLength(int originalLength) { this.originalLength = originalLength; return this; }
        public Builder summaryText(String summaryText) { this.summaryText = summaryText; return this; }
        public Builder method(String method) { this.method = method; return this; }
        public Builder createdAt(String createdAt) { this.createdAt = createdAt; return this; }

        public SummaryRecord build() {
            return new SummaryRecord(this);
        }
    }
}
