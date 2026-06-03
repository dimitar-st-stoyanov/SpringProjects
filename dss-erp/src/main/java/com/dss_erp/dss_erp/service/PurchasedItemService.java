package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.payload.PurchasedItemDTO;
import com.dss_erp.dss_erp.payload.PurchasedItemResponse;
import com.dss_erp.dss_erp.payload.SheetDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface PurchasedItemService {

    PurchasedItemDTO addPurchasedItem(PurchasedItemDTO item, Long categoryId);

    PurchasedItemResponse getAllPurchasedItems(int pageNumber, int pageSize, String sortBy, String sortOrder, String keyword, String category);

    PurchasedItemResponse searchByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder);

    PurchasedItemResponse searchPurchasedItemByKeyoword(String keyword,int pageNumber,int pageSize,String sortBy, String sortOrder);

    PurchasedItemDTO updatePurchasedItem(Long itemId, PurchasedItemDTO item);

    PurchasedItemDTO deletePurchased(Long itemId);

    PurchasedItemDTO increaseQuantity(Long id, Integer quantity);

    PurchasedItemDTO updatePurchasedItemImage(Long itemId, MultipartFile image) throws IOException;

    @Transactional
    PurchasedItemDTO consumePurchasedItem(Long purchasedItemId, Integer amount, String usedFor);
}
