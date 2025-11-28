package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RectTubePieceDTO {

    private Long id;

    // Same as TubePieceDTO: single length per piece
    private Double length;

    private String location;

    private Boolean isScrap;

    // Number of identical pieces (batching pieces by length)
    private Integer quantity;
}
