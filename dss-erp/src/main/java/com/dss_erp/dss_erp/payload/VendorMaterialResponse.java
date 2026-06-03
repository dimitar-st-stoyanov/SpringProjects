package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.BomComponentType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorMaterialResponse {

    private UUID id;

    private UUID vendorId;
    private String vendorName;

    private BomComponentType componentType;
    private Long componentId;

    private String vendorMaterialCode;
    private String vendorMaterialName;

    private Integer leadTimeDays;
    private Integer minimumOrderQuantity;
    private Integer orderMultiple;

    private boolean preferred;
    private boolean active;
}
