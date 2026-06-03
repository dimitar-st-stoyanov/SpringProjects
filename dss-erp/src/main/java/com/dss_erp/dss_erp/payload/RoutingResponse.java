package com.dss_erp.dss_erp.payload;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutingResponse {

    private List<RoutingDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}