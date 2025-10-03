package com.email_assistance.email_assistant.request;

public class OpenAIRequest {
    private String model;
    private String input;

    public OpenAIRequest(String model, String input) {
        this.model = model;
        this.input = input;
    }

    public String getModel() { return model; }
    public String getInput() { return input; }
}

