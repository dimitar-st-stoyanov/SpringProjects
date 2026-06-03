package com.dss_quotation.dss_quotation.payload;

import com.dss_quotation.dss_quotation.models.QuoteStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuoteStatusRequest {

    private QuoteStatus status;
}
