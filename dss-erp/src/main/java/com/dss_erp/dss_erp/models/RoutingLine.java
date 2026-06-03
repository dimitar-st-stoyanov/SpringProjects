package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routing_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutingLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer sequence;

    private String description;

    @Column(name = "setup_time", nullable = false)
    private Integer setupTime;

    @Column(name = "run_time", nullable = false)
    private Integer runTime; // per unit

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "routing_id")
    private Routing routing;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "work_center_id")
    private WorkCenter workCenter;
}