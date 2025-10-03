package com.email_assistance.email_assistant.request;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String tone;
}
