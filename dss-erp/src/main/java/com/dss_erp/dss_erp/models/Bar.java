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

    private Double width;          // mm
    private Double height;         // mm
    private Double standardLength; // mm

    /** Total available length (mm) of all BarPiece items */
    private Double quantity = 0.0;

    @OneToMany(mappedBy = "bar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BarPiece> pieces = new ArrayList<>();

    // ------------------------
    // Domain logic (mirrors RectTube)
    // ------------------------

    public void setPieces(List<BarPiece> pieces) {
        this.pieces = (pieces == null ? new ArrayList<>() : pieces);
    }

    /** Sum of all pieces: length * quantity */
    public double computeTotalLengthFromPieces() {
        if (pieces == null) return 0.0;
        return pieces.stream()
                .mapToDouble(p -> {
                    double len = p.getLength() != null ? p.getLength() : 0.0;
                    double qty = p.getQuantity() != null ? p.getQuantity() : 1.0;
                    return len * qty;
                })
                .sum();
    }

    /** Updates the Bar.quantity to match pieces */
    public void updateQuantityFromPieces() {
        this.quantity = computeTotalLengthFromPieces();
    }

    /** Number of non-scrap pieces */
    public long getAvailablePieces() {
        if (pieces == null) return 0;
        return pieces.stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsScrap()))
                .count();
    }

    // ------------------------
    // Name + Weight
    // ------------------------

    public void generateName() {
        setName("Rect Bar " + getGrade() + " " + width + "x" + height + "mm");
    }

    /** Weight calculation for solid rectangular bar */
    public void calculateWeight() {
        double W = width != null ? width : 0.0;
        double H = height != null ? height : 0.0;
        double L = standardLength != null ? standardLength : 0.0;
        double density = getDensity() != null ? getDensity() : 0.0;

        double w = W / 1000.0;        // m
        double h = H / 1000.0;        // m
        double lengthMeters = L / 1000.0;

        double volume = w * h * lengthMeters; // solid bar
        setWeight(volume * density);
    }
}
