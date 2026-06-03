package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.BomComponentType;
import com.dss_erp.dss_erp.models.BomStatus;
import com.dss_erp.dss_erp.models.Product;
import com.dss_erp.dss_erp.repositories.BomLineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.ComponentType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReverseBomServiceImpl implements ReverseBomService{


        private final BomLineRepository bomLineRepository;
        @Override
        public List<Product> whereUsed(
                Long componentId,
                BomComponentType componentType
        ) {
            return bomLineRepository.findWhereUsed(componentId,
                    componentType,
                    BomStatus.RELEASED);
        }
}

