package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Bom;
import com.dss_erp.dss_erp.models.BomExplosionRow;
import com.dss_erp.dss_erp.models.MaterialRequirementRow;
import com.dss_erp.dss_erp.models.Product;

import java.util.List;

public interface BomService {

    Bom createBom(Product product, String note);

    Bom cloneBom(Long bomId, String note);

    Bom getLatestBom(Long productId);

    List<Bom> getAllBoms(Long productId);

    Bom getBomById(Long bomId);

    Bom releaseBom(Long bomId);


    List<BomExplosionRow> explodeBom(Long productId, Long bomId);

    List<MaterialRequirementRow> explodeMaterials(
            Long productId,
            Long bomId
    );
}
