package com.dss_quotation.dss_quotation.payload;

import com.dss_quotation.dss_quotation.models.GasType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineCutParametersDetailsResponse {

    private Long id;

    private Long machineId;
    private String machineName;
    private double machinePower;

    private Long materialId;
    private String materialName;
    private String materialType;

    private double thickness;
    private double speed;
    private double pierceTime;
    private double pierceHeight;
    private GasType gasType;
}
