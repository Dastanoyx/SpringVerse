package com.sm.springverse.service.impl;

import com.sm.springverse.exception.AIServiceException;
import com.sm.springverse.service.BaseService;
import com.sm.springverse.service.TextToSpeechService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class TextToSpeechServiceImpl extends BaseService implements TextToSpeechService {

    public TextToSpeechServiceImpl(WebClient.Builder builder) {
        super(builder);
    }

    @Override
    public byte[] convertTextToSpeech(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                throw new IllegalArgumentException("Input text cannot be empty.");
            }

            String payload = "{\"inputs\": \"" + text + "\"}";

            return prepareRequest("/models/facebook/fastspeech2-en-ljspeech")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

        } catch (WebClientResponseException e) {
            throw new AIServiceException("Error generating speech from API: " + e.getResponseBodyAsString(),
                    "TEXT_TO_SPEECH_API_ERROR");
        } catch (IllegalArgumentException e) {
            throw new AIServiceException(e.getMessage(), "INVALID_INPUT");
        } catch (Exception e) {
            throw new AIServiceException("Unexpected error during text-to-speech conversion: " + e.getMessage(),
                    "TEXT_TO_SPEECH_ERROR");
        }
    }
}
