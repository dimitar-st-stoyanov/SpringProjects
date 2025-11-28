package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tubes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SequenceGenerator(
        name = "tube_seq",
        sequenceName = "tube_seq",
        initialValue = 120001,
        allocationSize = 1
)
public class Tube extends RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tube_seq")
    private Long id;

    private Double outerDiameter;   // mm
    private Double wallThickness;   // mm
    private Double standardLength;  // mm per new tube

    /** Stores total available length (mm) from all TubePieces */
    private Double quantity = 0.0;

    @OneToMany(mappedBy = "tube", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TubePiece> pieces = new ArrayList<>();

    // ------------------------
    // Domain logic
    // ------------------------

    public void setPieces(List<TubePiece> pieces) {
        this.pieces = (pieces == null ? new ArrayList<>() : pieces);
    }

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

    public long getAvailablePieces() {
        if (pieces == null) return 0;
        return pieces.stream().filter(p -> !Boolean.TRUE.equals(p.getIsScrap())).count();
    }

    public void generateName() {
        setName("Tube " + getGrade() + " " + outerDiameter + "x" + wallThickness + "mm");
    }

    public void calculateWeight() {
        double OD = outerDiameter != null ? outerDiameter : 0.0;
        double WT = wallThickness != null ? wallThickness : 0.0;
        double L = standardLength != null ? standardLength : 0.0;
        double density = getDensity() != null ? getDensity() : 0.0;

        double rOuter = OD / 2000.0;
        double rInner = (OD - 2 * WT) / 2000.0;
        double lengthMeters = L / 1000.0;

        double volume = Math.PI * (Math.pow(rOuter, 2) - Math.pow(rInner, 2)) * lengthMeters;
        setWeight(volume * density);
    }
}
