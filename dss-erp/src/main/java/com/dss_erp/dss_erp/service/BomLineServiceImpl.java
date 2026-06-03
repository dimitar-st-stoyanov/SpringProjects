package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.exceptions.ResourceNotFoundException;
import com.dss_erp.dss_erp.models.*;
import com.dss_erp.dss_erp.payload.BomLineDTO;
import com.dss_erp.dss_erp.payload.BomLineViewDTO;
import com.dss_erp.dss_erp.payload.PurchasePriceResponse;
import com.dss_erp.dss_erp.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BomLineServiceImpl implements BomLineService {

    private final BomLineRepository bomLineRepository;
    private final ProductRepository productRepository;
    private final BomRepository bomRepository;
    private final SheetRepository sheetRepository;
    private final BarRepository barRepository;
    private final TubeRepository tubeRepository;
    private final RectTubeRepository rectTubeRepository;
    private final RodRepository rodRepository;
    private final PurchasedItemRepository purchasedItemRepository;
    private final VendorMaterialRepository vendorMaterialRepository;
    private final PurchasePriceService purchasePriceService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public BomLine addBomLine(Long bomId, BomLineDTO dto) {

        Bom bom = bomRepository.findById(bomId)
                .orElseThrow(() -> new ResourceNotFoundException("BOM", "id", bomId));

        if (bom.getStatus() != BomStatus.DRAFT) {
            throw new IllegalStateException("Cannot modify a RELEASED BOM");
        }

        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }


        boolean exists = bomLineRepository
                .existsByBomIdAndComponentTypeAndComponentId(
                        bomId,
                        dto.getComponentType(),
                        dto.getComponentId()
                );

        if (exists) {
            throw new IllegalStateException("Component already exists in BOM");
        }

        validateComponentExists(dto.getComponentType(), dto.getComponentId());

        BomLine bomLine = new BomLine();
        bomLine.setBom(bom);
        bomLine.setComponentType(dto.getComponentType());
        bomLine.setComponentId(dto.getComponentId());
        bomLine.setQuantity(dto.getQuantity());
        bomLine.setUnit(dto.getUnit());

        return bomLineRepository.save(bomLine);
    }

    private void validateComponentExists(BomComponentType type, Long id) {
        switch (type) {
            case PRODUCT -> productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
            case SHEET -> sheetRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Sheet", "id", id));
            case TUBE -> tubeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Tube", "id", id));
            case RECT_TUBE -> rectTubeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("RectTube", "id", id));
            case BAR -> barRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Bar", "id", id));
            case ROD -> rodRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Rod", "id", id));
            case PURCHASED -> purchasedItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PurchasedItem", "id", id));
        }
    }



    @Override
    public List<BomLineViewDTO> getBom(Long bomId) {

        // 1️⃣ Fetch all BOM lines
        List<BomLine> lines = bomLineRepository.findByBomId(bomId);
        if (lines.isEmpty()) {
            return List.of();
        }

        // 2️⃣ Group component IDs by type
        Map<BomComponentType, Set<Long>> componentIdsByType = new HashMap<>();
        for (BomLine line : lines) {
            componentIdsByType
                    .computeIfAbsent(line.getComponentType(), k -> new HashSet<>())
                    .add(line.getComponentId());
        }

        // 3️⃣ Batch fetch preferred VendorMaterials
        Map<Long, VendorMaterial> vendorMaterialMap = new HashMap<>();
        for (Map.Entry<BomComponentType, Set<Long>> entry : componentIdsByType.entrySet()) {
            List<VendorMaterial> materials =
                    vendorMaterialRepository.findAllByComponentTypeAndComponentIdInAndPreferredTrueAndActiveTrue(
                            entry.getKey(),
                            entry.getValue()
                    );

            for (VendorMaterial vm : materials) {
                vendorMaterialMap.put(vm.getComponentId(), vm);
            }
        }

        // 4️⃣ Batch fetch prices for all vendorMaterials
        Set<UUID> vendorMaterialIds = vendorMaterialMap.values()
                .stream()
                .map(VendorMaterial::getId)
                .collect(Collectors.toSet());

        Map<UUID, PurchasePriceResponse> priceMap =
                purchasePriceService.getReferencePricesForVendorMaterials(vendorMaterialIds);

        // 5️⃣ Batch fetch components by type (optional: could replace switch with batch queries)
        //    Here we still use switch for simplicity

        List<BomLineViewDTO> result = new ArrayList<>();

        for (BomLine line : lines) {

            Long code = null;
            String name = null;

            switch (line.getComponentType()) {
                case PRODUCT -> {
                    Product p = productRepository.findById(line.getComponentId()).orElseThrow();
                    code = p.getId();
                    name = p.getName();
                }
                case SHEET -> {
                    Sheet s = sheetRepository.findById(line.getComponentId()).orElseThrow();
                    code = s.getId();
                    name = s.getName();
                }
                case TUBE -> {
                    Tube t = tubeRepository.findById(line.getComponentId()).orElseThrow();
                    code = t.getId();
                    name = t.getName();
                }
                case RECT_TUBE -> {
                    RectTube rt = rectTubeRepository.findById(line.getComponentId()).orElseThrow();
                    code = rt.getId();
                    name = rt.getName();
                }
                case BAR -> {
                    Bar b = barRepository.findById(line.getComponentId()).orElseThrow();
                    code = b.getId();
                    name = b.getName();
                }
                case ROD -> {
                    Rod r = rodRepository.findById(line.getComponentId()).orElseThrow();
                    code = r.getId();
                    name = r.getName();
                }
                case PURCHASED -> {
                    PurchasedItem pi = purchasedItemRepository.findById(line.getComponentId()).orElseThrow();
                    code = pi.getItemId();
                    name = pi.getItemName();
                }
            }

            // 6️⃣ Lookup price
            VendorMaterial vm = vendorMaterialMap.get(line.getComponentId());
            PurchasePriceResponse price = vm != null ? priceMap.get(vm.getId()) : null;

            result.add(
                    new BomLineViewDTO(
                            line.getId(),
                            line.getComponentType().name(),
                            line.getComponentId(),
                            code,
                            name,
                            line.getQuantity(),
                            line.getUnit(),
                            price != null ? price.getPrice() : null,
                            price != null ? price.getCurrency() : null
                    )
            );
        }

        return result;
    }

    @Transactional
    @Override
    public BomLineDTO deleteBomLine(Long id) {

        BomLine bomLine = bomLineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BomLine", "id", id));

        if (bomLine.getBom().getStatus() != BomStatus.DRAFT) {
            throw new IllegalStateException("Cannot delete line from RELEASED BOM");
        }

        bomLineRepository.delete(bomLine);
        return modelMapper.map(bomLine, BomLineDTO.class);
    }


    @Transactional
    @Override
    public BomLineDTO updateBomLine(Long id, BomLineDTO dto) {

        BomLine bomLine = bomLineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BomLine", "id", id));

        Bom bom = bomLine.getBom();

        if (bom.getStatus() != BomStatus.DRAFT) {
            throw new IllegalStateException("Cannot modify a RELEASED BOM");
        }

        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }


        boolean exists = bomLineRepository
                .existsByBomIdAndComponentTypeAndComponentIdAndIdNot(
                        bom.getId(),
                        dto.getComponentType(),
                        dto.getComponentId(),
                        id
                );

        if (exists) {
            throw new IllegalStateException("Component already exists in BOM");
        }

        validateComponentExists(dto.getComponentType(), dto.getComponentId());

        bomLine.setComponentType(dto.getComponentType());
        bomLine.setComponentId(dto.getComponentId());
        bomLine.setQuantity(dto.getQuantity());
        bomLine.setUnit(dto.getUnit());

        bomLineRepository.save(bomLine);


        return modelMapper.map(bomLine, BomLineDTO.class);
    }



}

