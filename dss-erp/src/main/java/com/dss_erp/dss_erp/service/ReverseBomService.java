package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.models.Product;
import org.hibernate.type.ComponentType;

import java.util.List;

public interface ReverseBomService {
    List<Product> whereUsed(
            Long componentId,
            BomComponentType componentType
    );
}
