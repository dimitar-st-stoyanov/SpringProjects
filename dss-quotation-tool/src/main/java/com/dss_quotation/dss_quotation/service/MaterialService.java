package com.dss_quotation.dss_quotation.service;


import com.dss_quotation.dss_quotation.models.Material;
import com.dss_quotation.dss_quotation.payload.MaterialDetailsResponse;
import com.dss_quotation.dss_quotation.payload.MaterialRequest;
import com.dss_quotation.dss_quotation.payload.MaterialResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MaterialService {

    List<Material> getAll();              // for admin
    List<Material> getActive();           // for dropdown

    Material getById(Long id);

    Page<MaterialResponse> getPaginated(int page, int size);

    Page<MaterialResponse> getActivePaginated(int page, int size);

    MaterialDetailsResponse getDetails(Long id);

    Material create(Material material);

    MaterialDetailsResponse create(MaterialRequest request);

    Material update(Long id, Material material);

    MaterialDetailsResponse update(Long id, MaterialRequest request);

    void delete(Long id);                 // soft delete (active = false)
}
