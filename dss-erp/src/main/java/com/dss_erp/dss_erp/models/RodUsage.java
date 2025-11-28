package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rod_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RodUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rodId;           // reference to the sheet
    private Double lengthUsed;   // number of sheets used
    private String usedFor;         // e.g., Job #123
    private LocalDateTime usedAt = LocalDateTime.now();
}
