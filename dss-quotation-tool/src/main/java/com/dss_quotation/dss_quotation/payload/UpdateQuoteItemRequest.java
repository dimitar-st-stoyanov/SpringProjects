package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuoteItemRequest {

    private Long itemId;
    private Long materialId;
    private Integer quantity;
    private Integer bends;
    private List<Long> operationIds;
    private List<QuoteItemOperationRequest> operations;
}
