package com.sm.springverse.service.impl;

import com.sm.springverse.exception.AIServiceException;
import com.sm.springverse.service.BaseService;
import com.sm.springverse.service.SentimentAnalysisService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SentimentAnalysisServiceImpl extends BaseService implements SentimentAnalysisService {

    public SentimentAnalysisServiceImpl(WebClient.Builder builder) {
        super(builder);
    }

    @Override
    public String analyzeSentiment(String text) {
        try {
            if (text == null || text.trim().isEmpty()) {
                throw new IllegalArgumentException("Input text cannot be empty.");
            }
            String payload = "{\"inputs\": \"" + text + "\"}";
            return prepareRequest("/models/cardiffnlp/twitter-roberta-base-sentiment")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (IllegalArgumentException e) {
            throw new AIServiceException(e.getMessage(), "INVALID_INPUT");
        } catch (Exception e) {
            throw new AIServiceException("Error analyzing sentiment: " + e.getMessage(),
                    "SENTIMENT_ANALYSIS_ERROR");
        }
    }
}
