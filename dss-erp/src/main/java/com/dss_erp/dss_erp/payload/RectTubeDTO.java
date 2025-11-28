package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RectTubeDTO {

    private Long id;

    private String grade;
    private String unit;      // mm, Kg, etc.
    private Double density;   // material density

    private Double width;         // mm
    private Double height;        // mm
    private Double wallThickness; // mm

    private Double standardLength; // mm (e.g., 6000mm per new tube)
    private Integer quantity;      // number of full new tubes in stock
    private Double weight;         // weight of ONE full-length tube
    private String name;

    // Aggregated stock information from all RectTubePiece entries
    private Double totalLength;
    private Double availableLength;
    private Long availablePieces; // optional: computed using density

    // Optional: piece-level details
    private List<RectTubePieceDTO> pieces;
}
