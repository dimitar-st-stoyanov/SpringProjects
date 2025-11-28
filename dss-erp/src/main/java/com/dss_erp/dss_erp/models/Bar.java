package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bars")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SequenceGenerator(
        name = "bar_seq",
        sequenceName = "bar_seq",
        initialValue = 130001,
        allocationSize = 1
)
public class Bar extends RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bar_seq")
    private Long id;

    private Double width;           // mm
    private Double height;          // mm
    private Double standardLength;  // mm per new bar (e.g. 6000)
    private Integer quantity;       // number of full-length bars received

    /** Linked physical pieces of this bar type */
    @OneToMany(mappedBy = "bar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BarPiece> pieces = new ArrayList<>();

    /**
     * Calculate total theoretical length (based on quantity × standard length)
     */
    public double getTotalLength() {
        return (standardLength != null && quantity != null)
                ? standardLength * quantity
                : 0.0;
    }

    /**
     * Calculate actual remaining stock length (sum of all pieces).
     */
    public double getAvailableLength() {
        if (pieces == null) return 0.0;
        return pieces.stream()
                .mapToDouble(p -> p.getRemainingLength() != null ? p.getRemainingLength() : 0.0)
                .sum();
    }

    /**
     * Returns total number of usable pieces (not marked as scrap).
     */
    public long getAvailablePieces() {
        if (pieces == null) return 0;
        return pieces.stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsScrap()))
                .count();
    }


    /**
     * Cut a piece and update its remaining length.
     */
    public void cutPiece(Long pieceId, double cutLengthMm) {
        BarPiece piece = pieces.stream()
                .filter(p -> p.getId().equals(pieceId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Piece not found"));

        if (cutLengthMm > piece.getRemainingLength()) {
            throw new IllegalArgumentException("Not enough length in this piece");
        }

        double remaining = piece.getRemainingLength() - cutLengthMm;
        piece.setRemainingLength(remaining);
        if (remaining < 100.0) { // threshold for scrap
            piece.setIsScrap(true);
        }
    }

    /**
     * Weight calculation for solid rectangular bar.
     */
    public void calculateWeight() {
        double safeWidth = (width != null) ? width : 0.0;
        double safeHeight = (height != null) ? height : 0.0;
        double safeLength = (standardLength != null) ? standardLength : 0.0;
        int safeQuantity = (quantity != null) ? quantity : 0;
        double safeDensity = (getDensity() != null) ? getDensity() : 0.0;

        // Convert mm to meters
        double w = safeWidth / 1000.0;
        double h = safeHeight / 1000.0;
        double l = safeLength / 1000.0;

        double volume = w * h * l * safeQuantity;

        setWeight(volume * safeDensity);
    }

    /**
     * Generate display name for the bar.
     */
    public void generateName() {
        setName("Rect Bar " + getGrade() + " " + width + "x" + height + "mm");
    }
}
