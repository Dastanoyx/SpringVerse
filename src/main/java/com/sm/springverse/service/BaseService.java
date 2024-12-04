package com.sm.springverse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class BaseService {

    protected final WebClient webClient;

    @Value("${huggingface.api.key}")
    private String apiKey;

    protected BaseService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api-inference.huggingface.co").build();
    }

    protected WebClient.RequestBodySpec prepareRequest(String uri) {
        return webClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
}
