package com.eccomerce.sb_ecomm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eccomerce.sb_ecomm.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
