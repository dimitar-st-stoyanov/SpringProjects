package com.dss_quotation.dss_quotation.payload;

import com.dss_quotation.dss_quotation.models.GasType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class MachineCutParametersRequest {

    @NotNull(message = "Machine is required")
    private Long machineId;

    @NotNull(message = "Material is required")
    private Long materialId;

    @NotNull(message = "Thickness is required")
    @Positive(message = "Thickness must be greater than zero")
    private Double thickness;

    @NotNull(message = "Speed is required")
    @Positive(message = "Speed must be greater than zero")
    private Double speed;

    @NotNull(message = "Pierce time is required")
    @PositiveOrZero(message = "Pierce time cannot be negative")
    private Double pierceTime;

    @NotNull(message = "Pierce height is required")
    @PositiveOrZero(message = "Pierce height cannot be negative")
    private Double pierceHeight;

    @NotNull(message = "Gas type is required")
    private GasType gasType;
}
