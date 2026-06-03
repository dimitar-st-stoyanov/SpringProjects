package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.BomComponentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorMaterialDTO {

    @NotNull
    private UUID vendorId;

    // Polymorphic component reference
    @NotNull
    private BomComponentType componentType;

    @NotNull
    @Positive
    private Long componentId;

    @Size(max = 100)
    private String vendorMaterialCode;

    @Size(max = 255)
    private String vendorMaterialName;

    @Positive
    private Integer leadTimeDays;

    @Positive
    private Integer minimumOrderQuantity;

    @Positive
    private Integer orderMultiple;

    private Boolean preferred;

    private Boolean active;
}
