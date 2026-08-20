package com.mindbrief;

import com.mindbrief.service.ExtractiveSummarizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExtractiveSummarizerTest {

    @Test
    void summarizeReturnsKeySentences() {
        String text = "Artificial intelligence is transforming healthcare. " +
                "Modern hospitals use AI to analyze medical images. " +
                "The technology also improves diagnostics and patient monitoring. " +
                "Many organizations still need clear governance and ethical oversight. " +
                "AI can reduce costs and increase efficiency across operations.";

        ExtractiveSummarizer summarizer = new ExtractiveSummarizer(3);
        String summary = summarizer.summarize(text);

        assertNotNull(summary);
        assertFalse(summary.isBlank());
        assertFalse(summary.length() < 20);
    }
}
