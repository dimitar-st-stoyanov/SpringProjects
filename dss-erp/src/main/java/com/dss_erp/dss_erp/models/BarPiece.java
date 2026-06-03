package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bar_pieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BarPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Length of this piece in mm */
    private Double length;

    /** Optional storage location */
    private String location;

    /** Whether this piece is marked as scrap */
    private Boolean isScrap = false;

    /** Number of identical pieces with the same length */
    @Column(nullable = false)
    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bar_id", nullable = false)
    private Bar bar;

    /**
     * Cuts this bar piece by the specified length.
     * Identical logic to RectTubePiece.cut()
     *
     * @param cutLength - length to cut in mm
     * @return leftover piece or null if none
     */
    public BarPiece cut(double cutLength) {

        if (cutLength <= 0) {
            throw new IllegalArgumentException("Cut length must be > 0");
        }
        if (cutLength > this.length) {
            throw new IllegalStateException("Cut length exceeds available length");
        }
        if (this.quantity <= 0) {
            throw new IllegalStateException("No quantity available to cut");
        }

        // Consume one piece
        this.quantity -= 1;

        double leftoverLength = this.length - cutLength;

        // No leftover piece
        if (leftoverLength <= 0) {
            return null;
        }

        // Create leftover piece (Qty = 1)
        BarPiece leftover = new BarPiece();
        leftover.setBar(this.bar);
        leftover.setLength(leftoverLength);
        leftover.setQuantity(1);
        leftover.setIsScrap(false);
        leftover.setLocation(this.location);

        return leftover;
    }
}
