package com.njf.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatDeepSeekController {

    private static final Logger logger = LoggerFactory.getLogger(ChatDeepSeekController.class);

    private final OllamaChatModel ollamaChatModel;

    public ChatDeepSeekController(OllamaChatModel ollamaChatModel) {
        this.ollamaChatModel = ollamaChatModel;
    }

    @GetMapping("/ai/test")
    public String generate(@RequestParam(value = "message", defaultValue = "hello") String message) {
        try {
            logger.info("Received message: {}", message);
            String response = this.ollamaChatModel.call(message);
            logger.info("Generated response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error generating response: {}", e.getMessage(), e);
            return "Error generating response: " + e.getMessage();
        }
    }
}
