package com.email_assistance.email_assistant.controllers;

import com.email_assistance.email_assistant.request.EmailRequest;
import com.email_assistance.email_assistant.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "https://mail.google.com")
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmailResponse(@RequestBody EmailRequest emailRequest) {
        String emailResponse = emailService.generateEmailResponse(emailRequest);
        return ResponseEntity.ok(emailResponse);
    }
}
