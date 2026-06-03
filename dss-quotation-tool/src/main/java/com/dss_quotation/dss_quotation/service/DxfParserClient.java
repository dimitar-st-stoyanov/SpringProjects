package com.dss_quotation.dss_quotation.service;


import com.dss_quotation.dss_quotation.exceptions.APIException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Component
public class DxfParserClient {

    private final WebClient webClient;

    public DxfParserClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8001")
                .build();
    }

    public Map<String, Object> parseDXF(byte[] fileBytes, String fileName) {
        File tempFile = convert(fileBytes, fileName);

        try {
            Map<String, Object> result = webClient.post()
                    .uri("/parse-dxf")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file",
                            new FileSystemResource(tempFile)))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            validateParserResult(result);

            return result;
        } catch (WebClientResponseException e) {
            throw new APIException("DXF parser failed: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new APIException("DXF parser service is unavailable or returned invalid data");
        } finally {
            if (tempFile.exists() && !tempFile.delete()) {
                tempFile.deleteOnExit();
            }
        }
    }

    private File convert(byte[] bytes, String fileName) {
        try {
            File file = File.createTempFile("upload-", sanitizeFileName(fileName));
            Files.write(file.toPath(), bytes);
            return file;
        } catch (IOException e) {
            throw new APIException("File conversion failed");
        }
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return ".dxf";
        }

        String sanitized = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        if (!sanitized.endsWith(".dxf")) {
            sanitized += ".dxf";
        }

        return sanitized;
    }

    private void validateParserResult(Map<String, Object> result) {
        if (result == null || result.isEmpty()) {
            throw new APIException("DXF parser returned empty result");
        }

        double cutLength = requirePositiveNumber(result, "cutLength");
        double minX = requireNumber(result, "minX");
        double minY = requireNumber(result, "minY");
        double maxX = requireNumber(result, "maxX");
        double maxY = requireNumber(result, "maxY");

        requireNonNegativeInteger(result, "pierceCount");

        if (cutLength <= 0) {
            throw new APIException("DXF file has no valid cut length");
        }

        if (maxX <= minX || maxY <= minY) {
            throw new APIException("DXF parser returned invalid dimensions");
        }
    }

    private double requireNumber(Map<String, Object> result, String key) {
        Object value = result.get(key);

        if (!(value instanceof Number)) {
            throw new APIException("DXF parser returned invalid value for: " + key);
        }

        return ((Number) value).doubleValue();
    }

    private double requirePositiveNumber(Map<String, Object> result, String key) {
        double value = requireNumber(result, key);

        if (value <= 0) {
            throw new APIException("DXF parser returned non-positive value for: " + key);
        }

        return value;
    }

    private int requireNonNegativeInteger(Map<String, Object> result, String key) {
        Object value = result.get(key);

        if (!(value instanceof Number)) {
            throw new APIException("DXF parser returned invalid value for: " + key);
        }

        int intValue = ((Number) value).intValue();

        if (intValue < 0) {
            throw new APIException("DXF parser returned negative value for: " + key);
        }

        return intValue;
    }
}

