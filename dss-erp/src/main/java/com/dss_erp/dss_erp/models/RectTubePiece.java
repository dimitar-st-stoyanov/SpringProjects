package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rect_tube_pieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RectTubePiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Length of a single piece (mm) */
    private Double length;

    /** Quantity of identical pieces with this length */
    @Column(nullable = false)
    private Integer quantity = 1;

    /** Location or remarks */
    private String location;

    /** Mark as scrap (unusable) */
    private Boolean isScrap = false;

    /** Many pieces belong to a single RectTube */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rect_tube_id", nullable = false)
    private RectTube rectTube;

    /**
     * Cut one piece from this batch, returning leftover if any.
     */
    public RectTubePiece cut(double cutLength) {
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

        // Exact cut → no leftover
        if (leftoverLength <= 0) {
            return null;
        }

        // Create leftover piece (qty = 1)
        RectTubePiece leftover = new RectTubePiece();
        leftover.setRectTube(this.rectTube);
        leftover.setLength(leftoverLength);
        leftover.setQuantity(1);
        leftover.setIsScrap(false);

        return leftover;
    }
}
