package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tube_pieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TubePiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double originalLength;    // mm (e.g. 6000)
    private Double remainingLength;   // mm
    private String location;          // optional: rack, shelf, bin
    private Boolean isScrap = false;  // short unusable leftover

    @Column(nullable = false)
    private Integer quantity = 1;     // number of identical tube pieces

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tube_id", nullable = false)
    private Tube tube;
}
