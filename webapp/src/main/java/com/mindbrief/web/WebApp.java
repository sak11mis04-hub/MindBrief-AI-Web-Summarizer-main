package com.mindbrief.web;

import com.mindbrief.service.ExtractiveSummarizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import spark.Spark;

import java.util.Map;

public class WebApp {
    public static void main(String[] args) {
        Spark.port(9090);
        Spark.staticFileLocation("/public");

        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return null;
        });

        Spark.post("/summarize", (req, res) -> {
            String text = req.queryParams("text");
            String url = req.queryParams("url");
            String method = req.queryParams("method");

            String sourceText = text;
            if (url != null && !url.isBlank()) {
                sourceText = fetchUrlText(url);
            }

            if (sourceText == null || sourceText.trim().isEmpty()) {
                res.status(400);
                return "Please provide text or a valid URL.";
            }

            String summary = "extractive".equalsIgnoreCase(method)
                    ? new ExtractiveSummarizer(3).summarize(sourceText)
                    : new ExtractiveSummarizer(3).summarize(sourceText);

            String html = "<html><head><title>MindBrief</title><style>body{font-family:Arial,sans-serif;padding:30px;background:#0f172a;color:#e2e8f0;} .card{background:#111827;border:1px solid #334155;border-radius:16px;padding:24px;max-width:900px;margin:auto;} h1{color:#c4b5fd;} pre{white-space:pre-wrap;line-height:1.6;}</style></head><body><div class='card'><h1>MindBrief Summary</h1><pre>" + summary.replace("<", "&lt;").replace(">", "&gt;") + "</pre><br><a href='/index.html'>Back</a></div></body></html>";
            return html;
        });

        System.out.println("MindBrief web app running at http://localhost:9090");
    }

    private static String fetchUrlText(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();
            doc.select("script, style, nav, footer, header, aside").remove();
            StringBuilder builder = new StringBuilder();
            doc.select("p").forEach(p -> {
                String text = p.text();
                if (text.length() > 50) builder.append(text).append(" ");
            });
            return builder.length() > 0 ? builder.toString().trim() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
