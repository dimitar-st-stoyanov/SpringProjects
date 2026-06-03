package com.dss_quotation.dss_quotation.payload;

import com.dss_quotation.dss_quotation.models.QuoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteFilterRequest {

    private String keyword;
    // quoteName, partName, customerName, fileName

    private Long machineId;
    private Long materialId;
    private QuoteStatus status;

    private Double minThickness;
    private Double maxThickness;

    private Integer minBends;
    private Integer maxBends;

    private Double minWeight;
    private Double maxWeight;

    private Double minPrice;
    private Double maxPrice;

    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;

    private String sortBy;
    private String sortDirection;

    private Integer page;
    private Integer size;

}

