package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bar_pieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BarPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Original full length of this piece when created (mm) */
    private Double originalLength;

    /** Current remaining length (mm) */
    private Double remainingLength;

    /** Whether this piece is marked as scrap (not usable) */
    private Boolean isScrap = false;

    /** Optional notes or remarks */
    private String remark;

    /** Many pieces belong to a single bar type */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bar_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Bar bar;

    /**
     * Mark this piece as scrap if remaining length below threshold.
     */
    public void updateScrapStatus(double scrapThresholdMm) {
        if (remainingLength != null && remainingLength < scrapThresholdMm) {
            isScrap = true;
        }
    }

    /**
     * Convenience method to safely get remaining length.
     */
    public double safeRemainingLength() {
        return remainingLength != null ? remainingLength : 0.0;
    }
}
