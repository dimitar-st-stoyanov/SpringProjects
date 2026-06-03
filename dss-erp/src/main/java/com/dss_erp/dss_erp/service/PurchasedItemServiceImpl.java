package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.exceptions.APIException;
import com.dss_erp.dss_erp.exceptions.ResourceNotFoundException;
import com.dss_erp.dss_erp.models.*;
import com.dss_erp.dss_erp.payload.PurchasedItemDTO;
import com.dss_erp.dss_erp.payload.PurchasedItemResponse;
import com.dss_erp.dss_erp.payload.SheetDTO;
import com.dss_erp.dss_erp.repositories.ItemCategoryRepository;
import com.dss_erp.dss_erp.repositories.PurchasedItemRepository;
import com.dss_erp.dss_erp.repositories.PurchasedItemUsageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PurchasedItemServiceImpl implements PurchasedItemService {

    @Autowired
    private PurchasedItemRepository purchasedItemRepository;

    @Autowired
    private ItemCategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PurchasedItemUsageRepository purchasedItemUsageRepository;

    // -------------------------------------------------------------
    // CREATE ITEM
    // -------------------------------------------------------------
    @Override
    public PurchasedItemDTO addPurchasedItem(PurchasedItemDTO itemDTO, Long categoryId) {

        ItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        // Check duplicates
        boolean exists = purchasedItemRepository.existsByItemNameAndCategory(itemDTO.getItemName(), category);
        if (exists) {
            throw new APIException("Product already exists!");
        }

        PurchasedItem entity = modelMapper.map(itemDTO, PurchasedItem.class);
        entity.setCategory(category);

        if (entity.getImage() == null) {
            entity.setImage("default.png");
        }

        PurchasedItem saved = purchasedItemRepository.save(entity);
        return modelMapper.map(saved, PurchasedItemDTO.class);
    }

    // -------------------------------------------------------------
    // GET ALL ITEMS WITH FILTER, SORT, PAGINATION
    // -------------------------------------------------------------
    @Override
    public PurchasedItemResponse getAllPurchasedItems(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortOrder,
            String keyword,
            String categoryName
    ) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Specification<PurchasedItem> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("itemName")), "%" + keyword.toLowerCase() + "%"));
        }

        if (categoryName != null && !categoryName.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("category").get("categoryName")), categoryName.toLowerCase()));
        }

        Page<PurchasedItem> page = purchasedItemRepository.findAll(spec, pageable);

        List<PurchasedItemDTO> dtoList = page.getContent()
                .stream()
                .map(item -> modelMapper.map(item, PurchasedItemDTO.class))
                .toList();

        return new PurchasedItemResponse(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // -------------------------------------------------------------
    // SEARCH BY CATEGORY ID
    // -------------------------------------------------------------
    @Override
    public PurchasedItemResponse searchByCategory(Long categoryId, int pageNumber, int pageSize,
                                                  String sortBy, String sortOrder) {

        ItemCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<PurchasedItem> page =
                purchasedItemRepository.findByCategoryOrderByItemNameAsc(category, pageable);

        List<PurchasedItemDTO> dtoList = page.getContent()
                .stream()
                .map(item -> modelMapper.map(item, PurchasedItemDTO.class))
                .toList();

        return new PurchasedItemResponse(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // -------------------------------------------------------------
    // SEARCH BY KEYWORD (NAME)
    // -------------------------------------------------------------
    @Override
    public PurchasedItemResponse searchPurchasedItemByKeyoword(String keyword, int pageNumber,
                                                               int pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<PurchasedItem> page =
                purchasedItemRepository.findByItemNameLikeIgnoreCase('%' + keyword + '%', pageable);

        List<PurchasedItemDTO> dtoList = page.getContent()
                .stream()
                .map(item -> modelMapper.map(item, PurchasedItemDTO.class))
                .toList();

        return new PurchasedItemResponse(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    // -------------------------------------------------------------
    // UPDATE ITEM
    // -------------------------------------------------------------
    @Override
    public PurchasedItemDTO updatePurchasedItem(Long itemId, PurchasedItemDTO dto) {

        PurchasedItem entity = purchasedItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchasedItem", "id", itemId));

        entity.setItemName(dto.getItemName());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        entity.setQuantity(dto.getQuantity());

        // Important: update attributes map
        if (dto.getAttributes() != null) {
            entity.setAttributes(dto.getAttributes());
        }

        PurchasedItem updated = purchasedItemRepository.save(entity);
        return modelMapper.map(updated, PurchasedItemDTO.class);
    }

    // -------------------------------------------------------------
    // DELETE ITEM
    // -------------------------------------------------------------
    @Override
    public PurchasedItemDTO deletePurchased(Long itemId) {

        PurchasedItem item = purchasedItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("PurchasedItem", "id", itemId));

        purchasedItemRepository.delete(item);
        return modelMapper.map(item, PurchasedItemDTO.class);
    }

    @Override
    public PurchasedItemDTO increaseQuantity(Long id, Integer quantity) {
        PurchasedItem purchasedItem = purchasedItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchased item not found with id " + id));

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Increase amount must be greater than zero");
        }

        purchasedItem.setQuantity(purchasedItem.getQuantity()+ quantity);

        PurchasedItem saved = purchasedItemRepository.save(purchasedItem);
        return modelMapper.map(saved, PurchasedItemDTO.class);


    }

    // -------------------------------------------------------------
    // UPDATE IMAGE
    // -------------------------------------------------------------
    @Override
    public PurchasedItemDTO updatePurchasedItemImage(Long itemId, MultipartFile image) throws IOException {
        // implement later when image storage is ready
        return null;
    }

    @Override
    public PurchasedItemDTO consumePurchasedItem(Long purchasedItemId, Integer amount, String usedFor) {
        PurchasedItem purchasedItem = purchasedItemRepository.findById(purchasedItemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id " + purchasedItemId));

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (purchasedItem.getQuantity() < amount) {
            throw new IllegalArgumentException("Not enough items in stock");
        }

        if (usedFor == null || usedFor.isBlank()) {
            throw new IllegalArgumentException("usedFor must be provided");
        }

        // 1️⃣ Reduce stock
        purchasedItem.setQuantity(purchasedItem.getQuantity() - amount);

        PurchasedItem saved = purchasedItemRepository.save(purchasedItem);

        // 2️⃣ Record usage
        PurchasedItemUsage usage = new PurchasedItemUsage();
        usage.setPurchasedItemId(purchasedItemId);
        usage.setQuantityUsed(amount);
        usage.setUsedFor(usedFor);
        purchasedItemUsageRepository.save(usage);

        return modelMapper.map(saved, PurchasedItemDTO.class);
    }
}
