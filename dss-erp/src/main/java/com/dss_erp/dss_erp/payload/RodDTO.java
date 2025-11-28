package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RodDTO {

    private Long id;
    private String grade;
    private String unit;
    private Double density;

    private Double diameter;       // same role as outerDiameter
    private Double standardLength; // mm per new rod
    private Integer quantity;
    private Double weight;
    private String name;

    // Aggregated stock info from RodPiece entries
    private Double totalLength;
    private Double availableLength;
    private Long availablePieces;

    // Optional: per-piece details
    private List<RodPieceDTO> pieces;
}
