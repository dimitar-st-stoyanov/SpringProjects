package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sheet_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sheetId;           // reference to the sheet
    private Integer quantityUsed;   // number of sheets used
    private String usedFor;         // e.g., Job #123
    private LocalDateTime usedAt = LocalDateTime.now();
}
