package com.sm.springverse.controller;


import com.sm.springverse.exception.InvalidRequestException;
import com.sm.springverse.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "AI Services", description = "Endpoints for AI-powered functionalities")
@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final SentimentAnalysisService sentimentService;
    private final ImageGenerationService imageService;
    private final QuestionAnsweringService qaService;
    private final TextSummarizationService summarizationService;
    private final TextToSpeechService ttsService;

    public AIController(SentimentAnalysisService sentimentService,
                        ImageGenerationService imageService,
                        QuestionAnsweringService qaService,
                        TextSummarizationService summarizationService,
                        TextToSpeechService ttsService) {
        this.sentimentService = sentimentService;
        this.imageService = imageService;
        this.qaService = qaService;
        this.summarizationService = summarizationService;
        this.ttsService = ttsService;
    }

    @PostMapping("/sentiment")
    @Operation(summary = "Analyze the sentiment of the provided text")
    public ResponseEntity<String> analyzeSentiment(@RequestBody String text) {
        return ResponseEntity.ok(sentimentService.analyzeSentiment(text));
    }

    @PostMapping("/image")
    @Operation(summary = "Generate an image based on the provided prompt")
    public ResponseEntity<byte[]> generateImage(@RequestBody String prompt) {
        byte[] image = imageService.generateImage(prompt);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .contentType(MediaType.IMAGE_PNG)
                .body(image);
    }


    @PostMapping("/qa")
    @Operation(summary = "Get an answer to a question based on the provided context")
    public ResponseEntity<String> getAnswer(@RequestBody Map<String, Object> body) {
        String question = (String) body.get("question");
        String context = (String) body.get("context");

        if (question == null || question.trim().isEmpty() || context == null || context.trim().isEmpty()) {
            throw new InvalidRequestException("'question' and 'context' fields are required and cannot be empty.");
        }

        String answer = qaService.getAnswer(question, context);
        return ResponseEntity.ok(answer);
    }



    @PostMapping("/summarize")
    @Operation(summary = "Summarize a block of text")
    public ResponseEntity<String> summarizeText(@RequestBody Map<String, String> body) {
        String text = body.get("text");

        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request: 'text' cannot be null or empty.");
        }

        return ResponseEntity.ok(summarizationService.summarizeText(text));
    }

    @PostMapping("/tts")
    @Operation(summary = "Convert text to speech")
    public ResponseEntity<byte[]> convertTextToSpeech(@RequestBody String text) {
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Input text cannot be empty.".getBytes());
        }

        byte[] audio = ttsService.convertTextToSpeech(text);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.wav\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audio);
    }
}
