package com.yambol_health.yambol_health.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHoursDTO {
    private Long id;
    @NotNull(message = "Pharmacy id is required") private Long pharmacyId;
    @NotNull(message = "Day of week is required") private DayOfWeek dayOfWeek;
    @NotNull(message = "Opening time is required") private LocalTime openTime;
    @NotNull(message = "Closing time is required") private LocalTime closeTime;
}
