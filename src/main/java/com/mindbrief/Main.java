package com.mindbrief;

import com.mindbrief.db.SummaryDAO;
import com.mindbrief.model.SummaryRecord;
import com.mindbrief.service.AISummarizer;
import com.mindbrief.service.ExtractiveSummarizer;
import com.mindbrief.service.SummarizerFactory;
import com.mindbrief.service.SummarizerStrategy;
import com.mindbrief.service.WebExtractor;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SummaryDAO dao = new SummaryDAO();

        System.out.println("=== MindBrief: AI-Powered Text Summarizer ===");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Summarize pasted text");
            System.out.println("2. Summarize from a URL");
            System.out.println("3. View history");
            System.out.println("4. Search history");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> handleTextSummarization(scanner, dao);
                    case "2" -> handleUrlSummarization(scanner, dao);
                    case "3" -> printHistory(dao.getAll());
                    case "4" -> {
                        System.out.print("Enter search keyword: ");
                        printHistory(dao.search(scanner.nextLine().trim()));
                    }
                    case "5" -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");
    }

    private static void handleTextSummarization(Scanner scanner, SummaryDAO dao) throws Exception {
        System.out.print("Paste your text: ");
        String text = scanner.nextLine();
        SummarizerStrategy strategy = chooseStrategy(scanner);
        String summary = strategy.summarize(text);
        System.out.println("\n--- Summary (" + strategy.getName() + ") ---\n" + summary);

        dao.save(new SummaryRecord.Builder()
                .source("Pasted Text")
                .originalLength(text.length())
                .summaryText(summary)
                .method(strategy.getName())
                .build());
    }

    private static void handleUrlSummarization(Scanner scanner, SummaryDAO dao) throws Exception {
        System.out.print("Enter URL: ");
        String url = scanner.nextLine().trim();
        System.out.println("Extracting content...");
        String text = WebExtractor.extractText(url);

        SummarizerStrategy strategy = chooseStrategy(scanner);
        String summary = strategy.summarize(text);
        System.out.println("\n--- Summary (" + strategy.getName() + ") ---\n" + summary);

        dao.save(new SummaryRecord.Builder()
                .source(url)
                .originalLength(text.length())
                .summaryText(summary)
                .method(strategy.getName())
                .build());
    }

    private static SummarizerStrategy chooseStrategy(Scanner scanner) {
        System.out.print("Choose method (1=AI-Powered, 2=Extractive/Offline): ");
        String choice = scanner.nextLine().trim();
        if ("1".equals(choice)) {
            try {
                return new AISummarizer();
            } catch (IllegalStateException e) {
                System.out.println("AI not configured, falling back to extractive mode.");
                return new ExtractiveSummarizer(3);
            }
        }
        return SummarizerFactory.create("extractive");
    }

    private static void printHistory(List<SummaryRecord> records) {
        if (records.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        for (SummaryRecord record : records) {
            System.out.println("\n" + record);
        }
    }
}
