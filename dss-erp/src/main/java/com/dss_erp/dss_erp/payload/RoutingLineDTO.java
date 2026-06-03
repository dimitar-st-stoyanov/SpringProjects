package com.dss_erp.dss_erp.payload;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutingLineDTO {

    private Long id;
    private Integer sequence;
    private String description;

    private Integer setupTime;
    private Integer runTime;

    private Long routingId;
    private Long workCenterId;
}
