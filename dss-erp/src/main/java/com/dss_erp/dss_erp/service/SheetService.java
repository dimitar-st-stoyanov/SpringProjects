package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Sheet;
import com.dss_erp.dss_erp.payload.SheetDTO;
import jakarta.transaction.Transactional;

public interface SheetService extends BaseMaterialService<SheetDTO> {
    SheetDTO increaseQuantity(Long id, Integer quantity);

    @Transactional
    SheetDTO consumeSheets(Long sheetId, Integer amount, String usedFor);
}
