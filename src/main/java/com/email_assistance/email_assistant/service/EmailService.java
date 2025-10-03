package com.email_assistance.email_assistant.service;

import com.email_assistance.email_assistant.request.EmailRequest;
import com.email_assistance.email_assistant.request.OpenAIRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailService {
    private final String apiSecreteKey;
    private final WebClient webiClient;

    public EmailService(
            WebClient.Builder webClientBuilder,
            @Value("${openai.api.baseurl}") String baseUrl,
            @Value("${openai.api.secrete}") String apiSecrete) {
        this.webiClient = webClientBuilder.baseUrl(baseUrl).build();
        this.apiSecreteKey = apiSecrete;
    }

    public String generateEmailResponse(EmailRequest emailRequest) {
        String prompt = generatePrompt(emailRequest);
        String requestBody = String.format("""
                {
                    "model": "gpt-4.1-mini",
                    "input": "$s"
                }
                """, prompt);

        String emailResponse = webiClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/v1/responses").build())
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.apiSecreteKey)
                .bodyValue(new OpenAIRequest("gpt-4.1-mini", prompt))
                .retrieve().bodyToMono(String.class).block();
        return extractResponseFromContent(emailResponse);
    }

    private String extractResponseFromContent(String emailResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(emailResponse);
            return rootNode.path("output").get(0)
                    .path("content").get(0)
                    .path("text").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generatePrompt(EmailRequest emailRequest) {
        String tone = (emailRequest.getTone() == null || emailRequest.getTone().isEmpty())
                ? "professional"
                : emailRequest.getTone();

        // Build the prompt
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI assistant. ")
                .append("Please draft a ")
                .append(tone)
                .append(" email response. ")
                .append("The original email content is: \n")
                .append(emailRequest.getEmailContent())
                .append("\n\n")
                .append("Respond appropriately, maintaining a ")
                .append(tone)
                .append(" tone.");
        return prompt.toString();
    }
}
