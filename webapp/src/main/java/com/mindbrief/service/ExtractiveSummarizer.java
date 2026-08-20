package com.mindbrief.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExtractiveSummarizer {
    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "the", "is", "at", "which", "on", "a", "an", "and", "or", "but", "in", "of",
            "to", "for", "with", "as", "by", "that", "this", "it", "was", "were", "are",
            "be", "been", "from", "into", "their", "them", "they", "we", "our", "you", "your"
    ));

    private final int sentenceCount;

    public ExtractiveSummarizer(int sentenceCount) {
        this.sentenceCount = sentenceCount;
    }

    public String summarize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] rawSentences = text.split("(?<=[.!?])\\s+");
        if (rawSentences.length <= sentenceCount) {
            return text;
        }

        List<String> sentences = new ArrayList<>(Arrays.asList(rawSentences));
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String sentence : sentences) {
            for (String word : cleanWords(sentence)) {
                if (!word.isBlank() && !STOPWORDS.contains(word)) {
                    wordFrequency.merge(word, 1, Integer::sum);
                }
            }
        }

        Map<Integer, Double> scores = new HashMap<>();
        for (int i = 0; i < sentences.size(); i++) {
            String[] words = cleanWords(sentences.get(i));
            double score = 0;
            for (String word : words) {
                if (!word.isBlank()) {
                    score += wordFrequency.getOrDefault(word, 0);
                }
            }
            scores.put(i, score / Math.max(1, words.length));
        }

        List<Integer> topIndices = new ArrayList<>(scores.keySet());
        topIndices.sort((a, b) -> Double.compare(scores.get(b), scores.get(a)));

        List<Integer> selected = new ArrayList<>(topIndices.subList(0, Math.min(sentenceCount, topIndices.size())));
        Collections.sort(selected);

        StringBuilder summary = new StringBuilder();
        for (int index : selected) {
            summary.append(sentences.get(index)).append(" ");
        }
        return summary.toString().trim();
    }

    private String[] cleanWords(String sentence) {
        return sentence.toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+");
    }
}
