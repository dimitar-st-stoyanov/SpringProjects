package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CreateQuoteRequest {

    private List<MultipartFile> files;
    private List<String> partNames;
    private List<Integer> quantities;
    private List<Double> thicknesses;
    private List<Integer> bends;
    private List<Long> materialIds;
    private List<String> operationIds;
    private List<String> operations;
    private int margin;
    private double minimumCharge;

    private Long machineId;

    private String quoteName;
    private String customerName;

}
