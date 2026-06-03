package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.BomStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BomDTO {

    private Long id;
    private Long productId; // link to product
    private Integer version;
    private BomStatus status;
    private String note;
    private LocalDateTime createdAt;
}
