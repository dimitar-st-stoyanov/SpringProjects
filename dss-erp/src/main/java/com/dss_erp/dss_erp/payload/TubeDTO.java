package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TubeDTO {
    private Long id;
    private String grade;
    private String unit;
    private Double density;
    private Double outerDiameter;
    private Double wallThickness;

    private Double standardLength; // renamed for clarity
    private Integer quantity;
    private Double weight;
    private String name;

    // Optional: show current total and available stock info
    private Double totalLength;
    private Double availableLength;
    private Long availablePieces;

    // Optional: nested per-piece details
    private List<TubePieceDTO> pieces;
}
