package com.dss_quotation.dss_quotation.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteDxfItemOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_dxf_item_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private QuoteDxfItem quoteDxfItem;

    @ManyToOne
    @JoinColumn(name = "operation_id", nullable = false)
    private Operation operation;

    private double timeMinutes;
    private double cost;
}
