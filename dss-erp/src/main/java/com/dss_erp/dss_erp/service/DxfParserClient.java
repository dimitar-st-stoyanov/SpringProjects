package com.dss_erp.dss_erp.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@Component
public class DxfParserClient {

    private final WebClient webClient;

    public DxfParserClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8001")
                .build();
    }

    public Map<String, Object> parseDXF(MultipartFile file) {

        File tempFile = convert(file);

        return webClient.post()
                .uri("/parse-dxf")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("file",
                        new FileSystemResource(tempFile)))
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

    private File convert(MultipartFile file) {
        try {
            File conv = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(conv);
            return conv;
        } catch (Exception e) {
            throw new RuntimeException("File conversion failed", e);
        }
    }
}