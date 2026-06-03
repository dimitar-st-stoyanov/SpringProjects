package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;

@Component
public class DxfFileValidator {

    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 5 MB
    private static final int SAMPLE_SIZE_BYTES = 8192;

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new APIException("DXF file is required");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new APIException("DXF file size must be 10 MB or less");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new APIException("DXF file name is required");
        }

        if (!fileName.toLowerCase().endsWith(".dxf")) {
            throw new APIException("Only .dxf files are allowed");
        }

        byte[] sample;
        try {
            byte[] bytes = file.getBytes();
            int length = Math.min(bytes.length, SAMPLE_SIZE_BYTES);
            sample = new byte[length];
            System.arraycopy(bytes, 0, sample, 0, length);
        } catch (IOException e) {
            throw new APIException("Failed to read DXF file");
        }

        rejectBinaryFile(sample);

        String content = decodeText(sample);

        validateDxfMarkers(content);
    }

    private void rejectBinaryFile(byte[] sample) {
        for (byte b : sample) {
            int value = b & 0xFF;

            if (value == 0) {
                throw new APIException("DXF file appears to be binary or corrupted");
            }

            boolean allowedControlChar =
                    value == 9 || value == 10 || value == 13;

            if (value < 32 && !allowedControlChar) {
                throw new APIException("DXF file contains invalid binary characters");
            }
        }
    }

    private String decodeText(byte[] sample) {
        try {
            return StandardCharsets.UTF_8
                    .newDecoder()
                    .decode(java.nio.ByteBuffer.wrap(sample))
                    .toString()
                    .toUpperCase();
        } catch (CharacterCodingException e) {
            throw new APIException("DXF file encoding is invalid");
        }
    }

    private void validateDxfMarkers(String content) {
        if (!content.contains("SECTION")) {
            throw new APIException("Invalid DXF file: missing SECTION marker");
        }

        if (!content.contains("HEADER") && !content.contains("ENTITIES")) {
            throw new APIException("Invalid DXF file: missing HEADER or ENTITIES section");
        }
    }
}

