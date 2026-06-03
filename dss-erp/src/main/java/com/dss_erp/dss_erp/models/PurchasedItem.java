package com.dss_erp.dss_erp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "purchased_items")
public class PurchasedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotBlank
    @Size(min = 3, message = "Item name must contain at least 3 characters")
    private String itemName;

    private String image;

    @NotBlank
    @Size(min = 6, message = "Item description must contain at least 6 characters")
    private String description;

    @NotBlank
    private String unit;

    private Integer quantity;

    // ✅ Hibernate 6 JSON mapping
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> attributes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ItemCategory category;
}
