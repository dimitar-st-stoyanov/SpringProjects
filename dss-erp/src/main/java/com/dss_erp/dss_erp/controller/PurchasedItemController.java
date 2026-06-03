package com.dss_erp.dss_erp.controller;

import com.dss_erp.dss_erp.config.AppConstants;
import com.dss_erp.dss_erp.payload.PurchasedItemDTO;
import com.dss_erp.dss_erp.payload.PurchasedItemResponse;
import com.dss_erp.dss_erp.payload.SheetDTO;
import com.dss_erp.dss_erp.service.PurchasedItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/materials/purchased")
public class PurchasedItemController {

    private final PurchasedItemService purchasedItemService;

    @Autowired
    public PurchasedItemController(PurchasedItemService purchasedItemService) {
        this.purchasedItemService = purchasedItemService;
    }

    // CREATE ITEM
    @PostMapping("/public/categories/{categoryId}/purchased-item")
    public ResponseEntity<PurchasedItemDTO> addPurchasedItem(
            @Valid @RequestBody PurchasedItemDTO purchasedItemDTO,
            @PathVariable Long categoryId) {

        PurchasedItemDTO saved = purchasedItemService.addPurchasedItem(purchasedItemDTO, categoryId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // GET ALL ITEMS (with optional keyword/category)
    @GetMapping("/public/purchased-items")
    public ResponseEntity<PurchasedItemResponse> getAllPurchasedItems(
            @RequestParam(required=false) String keyword,
            @RequestParam(required=false) String category,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_ITEMS_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        PurchasedItemResponse response =
                purchasedItemService.getAllPurchasedItems(pageNumber, pageSize, sortBy, sortOrder, keyword, category);

        return ResponseEntity.ok(response);
    }

    // GET ITEMS BY CATEGORY
    @GetMapping("/public/categories/{categoryId}/purchased-items")
    public ResponseEntity<PurchasedItemResponse> getPurchasedItemsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_ITEMS_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        PurchasedItemResponse response =
                purchasedItemService.searchByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(response);
    }

    // GET ITEMS BY KEYWORD
    @GetMapping("/public/purchased-items/keyword/{keyword}")
    public ResponseEntity<PurchasedItemResponse> getPurchasedItemsByKeyword(
            @PathVariable String keyword,
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_ITEMS_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortOrder
    ) {
        PurchasedItemResponse response =
                purchasedItemService.searchPurchasedItemByKeyoword(keyword, pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(response);
    }

    // OPTIONAL: UPDATE ITEM
    @PutMapping("/public/purchased-item/{itemId}")
    public ResponseEntity<PurchasedItemDTO> updatePurchasedItem(
            @PathVariable Long itemId,
            @RequestBody PurchasedItemDTO dto) {

        PurchasedItemDTO updated = purchasedItemService.updatePurchasedItem(itemId, dto);
        return ResponseEntity.ok(updated);
    }

    // OPTIONAL: DELETE ITEM
    @DeleteMapping("/public/purchased-item/{itemId}")
    public ResponseEntity<PurchasedItemDTO> deletePurchasedItem(@PathVariable Long itemId) {
        PurchasedItemDTO deleted = purchasedItemService.deletePurchased(itemId);
        return ResponseEntity.ok(deleted);
    }

    @PutMapping("/public/purchased-item/{itemId}/increase")
    public ResponseEntity<PurchasedItemDTO> increaseQuantity(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {

        PurchasedItemDTO updatedPurchasedItem = purchasedItemService.increaseQuantity(itemId, quantity);
        return ResponseEntity.ok(updatedPurchasedItem);
    }

    @PutMapping("/public/purchased-item/{itemId}/consume")
    public ResponseEntity<PurchasedItemDTO> consumePurchasedItem(
            @PathVariable Long itemId,
            @RequestParam Integer amount,
            @RequestParam String usedFor) {

        PurchasedItemDTO updatedPurchasedItem = purchasedItemService.consumePurchasedItem(itemId, amount, usedFor);
        return ResponseEntity.ok(updatedPurchasedItem);
    }
}
