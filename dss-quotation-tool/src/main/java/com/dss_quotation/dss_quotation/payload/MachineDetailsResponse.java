package com.dss_quotation.dss_quotation.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineDetailsResponse {

    private Long id;
    private String name;
    private double power;
    private double ratePerHour;
    private double efficiencyFactor;
    private double minimumCharge;
}
