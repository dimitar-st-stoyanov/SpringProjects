package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarDTO {

    private Long id;

    private String grade;
    private String unit;       // mm, Kg, etc.
    private Double density;    // material density

    private Double width;      // mm
    private Double height;     // mm

    private Double standardLength; // mm (e.g., 6000 mm per new bar)
    private Integer quantity;      // number of full new bars received
    private Double weight;         // weight of ONE full-length bar
    private String name;

    // Aggregated stock information from BarPiece entries
    private Double totalLength;     // sum(quantity × length) of all original bars
    private Double availableLength; // computed from BarPiece actual lengths
    private Long availablePieces;   // number of usable pieces (not scrap)

    // Optional: piece-level DTOs
    private List<BarPieceDTO> pieces;
}
