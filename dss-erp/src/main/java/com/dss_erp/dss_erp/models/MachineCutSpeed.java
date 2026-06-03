package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineCutSpeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double thickness;

    private double speed;

    @ManyToOne
    @JoinColumn(name = "machine_id")
    private Machine machine;
}
