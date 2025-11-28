package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rod_pieces")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RodPiece {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double length;      // mm
    private String location;
    private Boolean isScrap = false;

    @Column(nullable = false)
    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rod_id", nullable = false)
    private Rod rod;


    /** Cut logic (identical to TubePiece.cut) */
    public RodPiece cut(double cutLength) {

        if (cutLength <= 0) {
            throw new IllegalArgumentException("Cut length must be > 0");
        }
        if (cutLength > this.length) {
            throw new IllegalStateException("Cut length exceeds available length");
        }
        if (this.quantity <= 0) {
            throw new IllegalStateException("No quantity available to cut");
        }

        // Consume ONE piece
        this.quantity -= 1;

        double leftoverLength = this.length - cutLength;

        // Exact cut → no leftover
        if (leftoverLength <= 0) {
            return null;
        }

        // Create leftover rod piece (qty = 1)
        RodPiece leftover = new RodPiece();
        leftover.setRod(this.rod);
        leftover.setLength(leftoverLength);
        leftover.setQuantity(1);
        leftover.setIsScrap(false);

        return leftover;
    }
}
