package com.mindbrief.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebExtractor {
    public static String extractText(String url) throws Exception {
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();

        doc.select("script, style, nav, footer, header, aside, .ad, .advertisement").remove();

        Elements paragraphs = doc.select("article p, main p, p");
        StringBuilder content = new StringBuilder();
        for (Element paragraph : paragraphs) {
            String text = paragraph.text().trim();
            if (text.length() > 60) {
                content.append(text).append(" ");
            }
        }

        if (content.length() == 0) {
            throw new RuntimeException("Could not extract readable content from this URL.");
        }

        return content.toString().trim();
    }
}
