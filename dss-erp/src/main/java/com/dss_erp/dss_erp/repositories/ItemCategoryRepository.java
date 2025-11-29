package com.dss_erp.dss_erp.repositories;

import com.dss_erp.dss_erp.models.ItemCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    ItemCategory findByCategoryName(String categoryName);
}
