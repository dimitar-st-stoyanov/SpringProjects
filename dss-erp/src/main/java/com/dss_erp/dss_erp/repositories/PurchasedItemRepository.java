package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.ItemCategory;
import com.dss_erp.dss_erp.models.PurchasedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PurchasedItemRepository
        extends JpaRepository<PurchasedItem, Long>, JpaSpecificationExecutor<PurchasedItem> {

    // Check if product exists within a category
    boolean existsByItemNameAndCategory(String itemName, ItemCategory category);

    // Search by category with pagination
    Page<PurchasedItem> findByCategoryOrderByItemNameAsc(ItemCategory category, Pageable pageable);

    // Search by keyword (case-insensitive)
    Page<PurchasedItem> findByItemNameLikeIgnoreCase(String keyword, Pageable pageable);
}
