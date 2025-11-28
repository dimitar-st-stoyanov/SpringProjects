package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rect-tube_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class RectTubeUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rectTubeId;           // reference to the sheet
    private Double lengthUsed;   // number of sheets used
    private String usedFor;         // e.g., Job #123
    private LocalDateTime usedAt = LocalDateTime.now();
}