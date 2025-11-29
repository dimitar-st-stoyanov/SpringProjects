package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.ItemCategory;
import com.dss_erp.dss_erp.models.PurchasedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedItemRepository extends JpaRepository<PurchasedItem,Long>, JpaSpecificationExecutor<PurchasedItem> {
    Page<PurchasedItem> findByCategoryOrderByPriceAsc(ItemCategory category, Pageable pageDetails);

    Page<PurchasedItem> findByItemNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
