package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.models.PriceSourceType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchasePriceDTO {
    @NotNull
    private UUID vendorId;

    @NotNull
    private BomComponentType componentType;

    @NotNull
    private Long componentId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 9, fraction = 6)
    private BigDecimal price;

    @NotBlank
    @Size(min = 3, max = 3)
    private String currency; // ISO 4217

    @NotNull
    private PriceSourceType priceSource;

    @NotNull
    private LocalDate validFrom;

    private LocalDate validTo;

    @Size(max = 50)
    private String referenceNumber;

    @Min(1)
    private Integer minimumOrderQuantity;
}

