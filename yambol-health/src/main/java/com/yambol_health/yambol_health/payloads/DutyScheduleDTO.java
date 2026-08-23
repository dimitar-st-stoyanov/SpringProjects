package com.yambol_health.yambol_health.payloads;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DutyScheduleDTO {
    private Long id;
    @NotNull(message = "Pharmacy id is required") private Long pharmacyId;
    @NotNull(message = "Date is required") private LocalDate date;
    @NotNull(message = "Start time is required") private LocalTime startTime;
    @NotNull(message = "End time is required") private LocalTime endTime;
}
