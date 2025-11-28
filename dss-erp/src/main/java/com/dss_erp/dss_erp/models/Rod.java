package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SequenceGenerator(
        name = "rod_seq",
        sequenceName = "rod_seq",
        initialValue = 150001,
        allocationSize = 1
)
public class Rod extends RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rod_seq")
    private Long id;

    /** For rods (usually round), but can be any profile */
    private Double diameter;        // mm

    /** Standard length of a fresh rod from supplier */
    private Double standardLength;  // mm

    /** Total available length (mm), computed from RodPieces */
    private Double quantity = 0.0;

    @OneToMany(mappedBy = "rod", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RodPiece> pieces = new ArrayList<>();


    // -----------------------------
    // Domain logic
    // -----------------------------

    public void setPieces(List<RodPiece> pieces) {
        this.pieces = (pieces == null ? new ArrayList<>() : pieces);
    }

    /** Sum of all rod piece lengths * quantity */
    public double computeTotalLengthFromPieces() {
        if (pieces == null) return 0.0;
        return pieces.stream()
                .mapToDouble(p -> (p.getLength() != null ? p.getLength() : 0.0)
                        * (p.getQuantity() != null ? p.getQuantity() : 1))
                .sum();
    }

    public void updateQuantityFromPieces() {
        this.quantity = computeTotalLengthFromPieces();
    }

    /** Count only usable pieces (non-scrap) */
    public long getAvailablePieces() {
        if (pieces == null) return 0;
        return pieces.stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsScrap()))
                .count();
    }

    /** Example: Rod 4140 Ø20mm */
    public void generateName() {
        setName("Rod " + getGrade() + " Ø" + diameter + "mm");
    }

    /** Weight calculation (solid round bar) */
    public void calculateWeight() {
        double D = diameter != null ? diameter : 0.0;
        double L = standardLength != null ? standardLength : 0.0;
        double density = getDensity() != null ? getDensity() : 0.0;

        double radius = D / 2000.0;
        double lengthMeters = L / 1000.0;

        double volume = Math.PI * Math.pow(radius, 2) * lengthMeters;
        setWeight(volume * density);
    }
}
