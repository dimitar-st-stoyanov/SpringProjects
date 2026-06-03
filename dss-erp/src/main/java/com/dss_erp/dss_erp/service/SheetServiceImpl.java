package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.PurchasedItem;
import com.dss_erp.dss_erp.models.Sheet;
import com.dss_erp.dss_erp.models.SheetUsage;
import com.dss_erp.dss_erp.payload.BaseMaterialResponse;
import com.dss_erp.dss_erp.payload.PurchasedItemDTO;
import com.dss_erp.dss_erp.payload.PurchasedItemResponse;
import com.dss_erp.dss_erp.payload.SheetDTO;
import com.dss_erp.dss_erp.repositories.SheetRepository;
import com.dss_erp.dss_erp.repositories.SheetUsageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SheetServiceImpl implements SheetService {

    private final SheetRepository sheetRepository;
    private final SheetUsageRepository sheetUsageRepository;
    private final ModelMapper modelMapper;

    @Override
    public SheetDTO create(SheetDTO dto) {
        Sheet sheet = modelMapper.map(dto, Sheet.class);
        sheet.calculateWeight();
        sheet.generateName();
        Sheet saved = sheetRepository.save(sheet);
        return modelMapper.map(saved, SheetDTO.class);
    }

    @Override
    public SheetDTO increaseQuantity(Long id, Integer amount) {
        Sheet sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found with id " + id));

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Increase amount must be greater than zero");
        }

        sheet.setQuantity(sheet.getQuantity() + amount);
        sheet.calculateWeight(); // update weight for total quantity
        Sheet saved = sheetRepository.save(sheet);

        return modelMapper.map(saved, SheetDTO.class);
    }

    @Override
    @Transactional
    public SheetDTO consumeSheets(Long sheetId, Integer amount, String usedFor) {
        Sheet sheet = sheetRepository.findById(sheetId)
                .orElseThrow(() -> new RuntimeException("Sheet not found with id " + sheetId));

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (sheet.getQuantity() < amount) {
            throw new IllegalArgumentException("Not enough sheets in stock");
        }

        if (usedFor == null || usedFor.isBlank()) {
            throw new IllegalArgumentException("usedFor must be provided");
        }

        // 1️⃣ Reduce stock
        sheet.setQuantity(sheet.getQuantity() - amount);
        sheet.calculateWeight();
        Sheet saved = sheetRepository.save(sheet);

        // 2️⃣ Record usage
        SheetUsage usage = new SheetUsage();
        usage.setSheetId(sheetId);
        usage.setQuantityUsed(amount);
        usage.setUsedFor(usedFor);
        sheetUsageRepository.save(usage);

        return modelMapper.map(saved, SheetDTO.class);
    }

    @Override
    public void delete(Long id) {
        sheetRepository.deleteById(id);
    }

    @Override
    public SheetDTO getById(Long id) {
        Sheet sheet = sheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sheet not found with ID: " + id));
        return modelMapper.map(sheet, SheetDTO.class);
    }

    @Override
    public BaseMaterialResponse<SheetDTO> getAll(int pageNumber, int pageSize, String sortBy, String sortOrder, String keyword) {

        if (sortBy == null || sortBy.isBlank()) {
            sortBy = "id";
        }

        Sort sort = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Specification<Sheet> spec = Specification.where(null);

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
        }

        Page<Sheet> page = sheetRepository.findAll(spec, pageable);

        List<SheetDTO> dtoList = page.getContent()
                .stream()
                .map(item -> modelMapper.map(item, SheetDTO.class))
                .toList();

        return new BaseMaterialResponse<SheetDTO>(
                dtoList,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

//    @Override
//    public List<SheetDTO> getAll() {
//        return sheetRepository.findAll()
//                .stream()
//                .map(sheet -> modelMapper.map(sheet, SheetDTO.class))
//                .toList();
//    }
}
