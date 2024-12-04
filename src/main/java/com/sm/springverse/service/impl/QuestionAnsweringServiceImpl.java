package com.sm.springverse.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.springverse.exception.ServiceProcessingException;
import com.sm.springverse.service.BaseService;
import com.sm.springverse.service.QuestionAnsweringService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.Map;


@Service
public class QuestionAnsweringServiceImpl extends BaseService implements QuestionAnsweringService {

    private final ObjectMapper objectMapper;

    public QuestionAnsweringServiceImpl(WebClient.Builder builder, ObjectMapper objectMapper) {
        super(builder);
        this.objectMapper = objectMapper;
    }

    @Override
    public String getAnswer(String question, String context) {
        try {
            Map<String, Object> payload = Map.of(
                    "inputs", Map.of(
                            "question", question,
                            "context", context
                    )
            );

            String response = prepareRequest("/models/deepset/roberta-base-squad2")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode jsonResponse = objectMapper.readTree(response);
            if (jsonResponse.has("answer")) {
                return jsonResponse.get("answer").asText();
            } else {
                throw new ServiceProcessingException("API response does not contain the 'answer' field.");
            }

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                throw new ServiceProcessingException("Service is currently unavailable. Please try again later.", e);
            }
            throw e;
        } catch (Exception e) {
            throw new ServiceProcessingException("Error processing QA service response.", e);
        }
    }
}
