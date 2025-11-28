package com.dss_erp.dss_erp.service;

import java.util.List;

public interface BaseMaterialService<T> {
    T create(T dto);
    void delete(Long id);
    T getById(Long id);
    List<T> getAll();
}
