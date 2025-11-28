package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rect_tubes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SequenceGenerator(
        name = "rect_seq",
        sequenceName = "rect_seq",
        initialValue = 140001,
        allocationSize = 1
)
public class RectTube extends RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rect_seq")
    private Long id;

    private Double width;          // mm
    private Double height;         // mm
    private Double wallThickness;  // mm
    private Double standardLength; // mm

    /** Total available length (mm) of all RectTubePiece items */
    private Double quantity = 0.0;

    @OneToMany(mappedBy = "rectTube", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RectTubePiece> pieces = new ArrayList<>();

    // ------------------------
    // Domain logic (now identical to Tube)
    // ------------------------

    public void setPieces(List<RectTubePiece> pieces) {
        this.pieces = (pieces == null ? new ArrayList<>() : pieces);
    }

    /** Sum of all pieces: length * quantity (identical to Tube) */
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

    /** Updates the RectTube.quantity to match pieces (identical to Tube) */
    public void updateQuantityFromPieces() {
        this.quantity = computeTotalLengthFromPieces();
    }

    /** Number of non-scrap pieces (same as Tube) */
    public long getAvailablePieces() {
        if (pieces == null) return 0;
        return pieces.stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsScrap()))
                .count();
    }

    // ------------------------
    // Name + Weight (kept same as before)
    // ------------------------

    public void generateName() {
        setName("Rect Tube " + getGrade() + " " + width + "x" + height + "x" + wallThickness + "mm");
    }

    /** Weight identical style to Tube.calculateWeight() */
    public void calculateWeight() {
        double W = width != null ? width : 0.0;
        double H = height != null ? height : 0.0;
        double WT = wallThickness != null ? wallThickness : 0.0;
        double L = standardLength != null ? standardLength : 0.0;
        double density = getDensity() != null ? getDensity() : 0.0;

        double w = W / 1000.0;
        double h = H / 1000.0;
        double t = WT / 1000.0;
        double lengthMeters = L / 1000.0;

        double outerVolume = w * h * lengthMeters;
        double innerVolume = (w - 2 * t) * (h - 2 * t) * lengthMeters;
        double netVolume = outerVolume - innerVolume;

        setWeight(netVolume * density);
    }
}
