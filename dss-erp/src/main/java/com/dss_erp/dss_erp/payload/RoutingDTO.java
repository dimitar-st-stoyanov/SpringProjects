package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.RoutingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutingDTO {

    private Long id;
    private Long productId;
    private Integer version;
    private RoutingStatus status;
    private String note;
    //private LocalDateTime createdAt;
}
