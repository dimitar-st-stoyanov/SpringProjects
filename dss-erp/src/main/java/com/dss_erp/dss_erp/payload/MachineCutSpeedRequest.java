package com.dss_erp.dss_erp.payload;

import lombok.Data;

@Data
public class MachineCutSpeedRequest {
    private Long machineId;
    private double thickness;
    private double speed;
}
