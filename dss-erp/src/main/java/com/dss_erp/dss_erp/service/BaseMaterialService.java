package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.BaseMaterialResponse;

public interface BaseMaterialService<T> {


        T create(T dto);

        void delete(Long id);

        T getById(Long id);

        BaseMaterialResponse<T> getAll(
                int pageNumber,
                int pageSize,
                String sortBy,
                String sortOrder,
                String keyword
        );

}
