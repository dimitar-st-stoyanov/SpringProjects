package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.PriceSourceType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasePriceResponse {

    private UUID id;

    private UUID vendorMaterialId;

    private BigDecimal price;

    private String currency;

    private PriceSourceType priceSource;

    private LocalDate validFrom;

    private LocalDate validTo;

    private String referenceNumber;

    private Integer minimumOrderQuantity;

    private boolean active;
}
