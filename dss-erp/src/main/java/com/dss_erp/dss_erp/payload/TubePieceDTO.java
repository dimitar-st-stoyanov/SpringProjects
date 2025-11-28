package com.dss_erp.dss_erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TubePieceDTO {

    private Long id;
    private Double length;
    private String location;
    private Boolean isScrap;
    private Integer quantity; // number of identical tube pieces
}
