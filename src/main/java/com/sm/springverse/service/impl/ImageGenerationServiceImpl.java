package com.sm.springverse.service.impl;

import com.sm.springverse.exception.AIServiceException;
import com.sm.springverse.service.BaseService;
import com.sm.springverse.service.ImageGenerationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ImageGenerationServiceImpl extends BaseService implements ImageGenerationService {

    public ImageGenerationServiceImpl(WebClient.Builder builder) {
        super(builder);
    }

    @Override
    public byte[] generateImage(String prompt) {
        try {
            String payload = "{\"inputs\": \"" + prompt + "\"}";
            return prepareRequest("/models/stabilityai/stable-diffusion-2")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new AIServiceException("Error generating image from API: " + e.getResponseBodyAsString(),
                    "IMAGE_GENERATION_ERROR");
        } catch (Exception e) {
            throw new AIServiceException("Unexpected error generating image: " + e.getMessage(),
                    "UNEXPECTED_ERROR");
        }
    }
}
