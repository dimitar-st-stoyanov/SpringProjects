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

    private Double length;    // mm
    private String location;
    private Boolean isScrap = false;

    @Column(nullable = false)
    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tube_id", nullable = false)
    private Tube tube;

    public TubePiece cut(double cutLength) {

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

        // Create leftover piece (qty = 1)
        TubePiece leftover = new TubePiece();
        leftover.setTube(this.tube);
        leftover.setLength(leftoverLength);
        leftover.setQuantity(1);
        leftover.setIsScrap(false);

        return leftover;
    }

}
