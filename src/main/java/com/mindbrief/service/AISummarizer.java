package com.mindbrief.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AISummarizer implements SummarizerStrategy {
    private final String apiKey;
    private final String apiUrl;

    public AISummarizer() {
        this.apiKey = System.getenv("OPENAI_API_KEY");
        this.apiUrl = "https://api.openai.com/v1/chat/completions";
        if (this.apiKey == null || this.apiKey.isBlank()) {
            throw new IllegalStateException("OPENAI_API_KEY environment variable not set. Use the extractive option or set the key.");
        }
    }

    @Override
    public String summarize(String text) throws Exception {
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", "Summarize the following text in 3-4 concise sentences, capturing the key points:\n\n" + text);

        JSONObject body = new JSONObject();
        body.put("model", "gpt-4o-mini");
        body.put("messages", new JSONArray().put(message));
        body.put("temperature", 0.5);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("AI API call failed: " + response.body());
        }

        JSONObject json = new JSONObject(response.body());
        return json.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content")
                .trim();
    }

    @Override
    public String getName() {
        return "AI-Powered (LLM)";
    }
}
