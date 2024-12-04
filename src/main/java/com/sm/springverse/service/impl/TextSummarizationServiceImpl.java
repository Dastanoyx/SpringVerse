package com.sm.springverse.service.impl;

import com.sm.springverse.exception.AIServiceException;
import com.sm.springverse.service.BaseService;
import com.sm.springverse.service.TextSummarizationService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class TextSummarizationServiceImpl extends BaseService implements TextSummarizationService {

    public TextSummarizationServiceImpl(WebClient.Builder builder) {
        super(builder);
    }

    @Override
    public String summarizeText(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                throw new IllegalArgumentException("Input text cannot be empty.");
            }

            Map<String, String> payload = Map.of("inputs", text);

            List<Map<String, String>> response = prepareRequest("/models/facebook/bart-large-cnn")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, String>>>() {})
                    .block();

            if (response != null && !response.isEmpty()) {
                return response.get(0).getOrDefault("summary_text", "No summary generated.");
            } else {
                throw new AIServiceException("API returned an empty response.", "SUMMARIZATION_EMPTY_RESPONSE");
            }

        } catch (WebClientResponseException e) {
            throw new AIServiceException("Error summarizing text from API: " + e.getResponseBodyAsString(),
                    "SUMMARIZATION_API_ERROR");
        } catch (IllegalArgumentException e) {
            throw new AIServiceException(e.getMessage(), "INVALID_INPUT");
        } catch (Exception e) {
            throw new AIServiceException("Unexpected error during text summarization: " + e.getMessage(),
                    "SUMMARIZATION_ERROR");
        }
    }
}
