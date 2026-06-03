package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import com.dss_quotation.dss_quotation.exceptions.ResourceNotFoundException;
import com.dss_quotation.dss_quotation.models.Machine;
import com.dss_quotation.dss_quotation.models.MachineCutParameters;
import com.dss_quotation.dss_quotation.models.Material;
import com.dss_quotation.dss_quotation.models.Operation;
import com.dss_quotation.dss_quotation.models.Quote;
import com.dss_quotation.dss_quotation.models.QuoteDxfItem;
import com.dss_quotation.dss_quotation.models.QuoteDxfItemOperation;
import com.dss_quotation.dss_quotation.models.QuoteStatus;
import com.dss_quotation.dss_quotation.payload.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dss_quotation.dss_quotation.repositories.QuoteDxfItemRepository;
import com.dss_quotation.dss_quotation.repositories.QuoteRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private static final double SHEET_MARGIN_MM = 20;

    private final DxfParserClient dxfParserClient;
    private final MachineService machineService;
    private final MachineCutParametersService machineCutParametersService;
    private final QuoteRepository quoteRepository;
    private final QuoteDxfItemRepository quoteDxfItemRepository;
    private final MaterialService materialService;
    private final OperationService operationService;
    private final DxfFileValidator dxfFileValidator;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public QuoteDetailsResponse generateQuote(CreateQuoteRequest request) {

        validateCreateQuoteRequest(request);

        Machine machine = machineService.getById(request.getMachineId());

        Quote quote = Quote.builder()
                .machine(machine)
                .quoteName(request.getQuoteName())
                .customerName(request.getCustomerName())
                .margin(request.getMargin())
                .minimumCharge(resolveMinimumCharge(request.getMinimumCharge(), machine))
                .status(QuoteStatus.DRAFT)
                .finalPriceOverridden(false)
                .createdAt(LocalDateTime.now())
                .build();

        for (int i = 0; i < request.getFiles().size(); i++) {

            Material material = materialService.getById(
                    request.getMaterialIds().get(i)
            );

            QuoteDxfItem item = buildDxfItem(
                    quote,
                    request.getFiles().get(i),
                    request.getPartNames().get(i),
                    resolveQuantity(request, i),
                    request.getThicknesses().get(i),
                    request.getBends().get(i),
                    material,
                    resolveOperationRequests(request, i),
                    machine,
                    request.getMargin()
            );

            quote.getItems().add(item);
        }

        applyTotals(quote);

        Quote savedQuote = quoteRepository.save(quote);

        return mapToDetailsResponse(savedQuote);
    }

    @Override
    @Transactional
    public QuoteDetailsResponse updateQuote(Long id, UpdateQuoteRequest request) {
        if (request == null) {
            throw new APIException("Quote update request is required");
        }

        Quote quote = getById(id);
        if (!isDraft(quote)) {
            throw new APIException("Only draft quotes can be edited");
        }

        Machine machine = quote.getMachine();
        if (request.getMachineId() != null) {
            machine = machineService.getById(request.getMachineId());
            quote.setMachine(machine);

            if (request.getMinimumCharge() == null) {
                quote.setMinimumCharge(machine.getMinimumCharge());
            }
        }

        if (request.getQuoteName() != null) {
            validateNotBlank(request.getQuoteName(), "Quote name");
            quote.setQuoteName(request.getQuoteName());
        }

        if (request.getCustomerName() != null) {
            validateNotBlank(request.getCustomerName(), "Customer name");
            quote.setCustomerName(request.getCustomerName());
        }

        if (request.getMargin() != null) {
            if (request.getMargin() < 0) {
                throw new APIException("Margin cannot be negative");
            }
            quote.setMargin(request.getMargin());
        }

        if (request.getMinimumCharge() != null) {
            if (request.getMinimumCharge() < 0) {
                throw new APIException("Minimum charge cannot be negative");
            }
            quote.setMinimumCharge(request.getMinimumCharge());
        }

        if (request.getFinalPrice() != null) {
            if (request.getFinalPrice() < 0) {
                throw new APIException("Final price cannot be negative");
            }
            quote.setTotalPrice(request.getFinalPrice());
            quote.setFinalPriceOverridden(true);
        }

        if (request.getItems() != null) {
            for (UpdateQuoteItemRequest itemRequest : request.getItems()) {
                updateQuoteItem(quote, itemRequest);
            }
        }

        for (QuoteDxfItem item : quote.getItems()) {
            recalculateItem(item, quote.getMachine(), quote.getMargin());
        }
        applyTotals(quote);

        return mapToDetailsResponse(quoteRepository.save(quote));
    }

    @Override
    @Transactional
    public QuoteDetailsResponse updateStatus(Long id, UpdateQuoteStatusRequest request) {
        if (request == null || request.getStatus() == null) {
            throw new APIException("Quote status is required");
        }

        Quote quote = getById(id);
        validateStatusTransition(resolveStatus(quote), request.getStatus());
        quote.setStatus(request.getStatus());

        return mapToDetailsResponse(quoteRepository.save(quote));
    }

    @Override
    @Transactional(readOnly = true)
    public QuoteDetailsResponse getDetails(Long id) {
        return mapToDetailsResponse(getById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Quote getById(Long id) {
        return quoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quote", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public QuoteDxfItem getDxfItem(Long quoteId, Long itemId) {
        return quoteDxfItemRepository.findByIdAndQuoteId(itemId, quoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Quote DXF item", "id", itemId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuoteListResponse2> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return quoteRepository.findAll(pageable)
                .map(this::mapToListResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuoteListResponse2> filter(QuoteFilterRequest request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        if (request.getPage() != null && request.getPage() < 0) {
            throw new APIException("Page number cannot be negative");
        }

        if (request.getSize() != null && request.getSize() <= 0) {
            throw new APIException("Page size must be greater than zero");
        }

        String sortBy = resolveSortBy(request.getSortBy());

        Sort.Direction direction = "asc".equalsIgnoreCase(request.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return quoteRepository.findAll((root, query, cb) -> {
            if (query != null) {
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();
            Join<Quote, QuoteDxfItem> items = root.join("items", JoinType.LEFT);

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                String keyword = "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("quoteName")), keyword),
                        cb.like(cb.lower(root.get("customerName")), keyword),
                        cb.like(cb.lower(items.get("partName")), keyword),
                        cb.like(cb.lower(items.get("fileName")), keyword)
                ));
            }

            if (request.getMachineId() != null) {
                predicates.add(cb.equal(root.get("machine").get("id"), request.getMachineId()));
            }

            if (request.getMaterialId() != null) {
                predicates.add(cb.equal(items.get("material").get("id"), request.getMaterialId()));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            if (request.getMinThickness() != null) {
                predicates.add(cb.greaterThanOrEqualTo(items.get("thickness"), request.getMinThickness()));
            }

            if (request.getMaxThickness() != null) {
                predicates.add(cb.lessThanOrEqualTo(items.get("thickness"), request.getMaxThickness()));
            }

            if (request.getMinBends() != null) {
                predicates.add(cb.greaterThanOrEqualTo(items.get("bends"), request.getMinBends()));
            }

            if (request.getMaxBends() != null) {
                predicates.add(cb.lessThanOrEqualTo(items.get("bends"), request.getMaxBends()));
            }

            if (request.getMinWeight() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalWeight"), request.getMinWeight()));
            }

            if (request.getMaxWeight() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalWeight"), request.getMaxWeight()));
            }

            if (request.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalPrice"), request.getMinPrice()));
            }

            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalPrice"), request.getMaxPrice()));
            }

            if (request.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), request.getCreatedFrom()));
            }

            if (request.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), request.getCreatedTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(this::mapToListResponse);
    }

    private QuoteDxfItem buildDxfItem(
            Quote quote,
            MultipartFile file,
            String partName,
            int quantity,
            double thickness,
            int bends,
            Material material,
            List<QuoteItemOperationRequest> operations,
            Machine machine,
            int margin
    ) {
        dxfFileValidator.validate(file);

        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new APIException("Failed to read DXF file");
        }

        Map<String, Object> result = dxfParserClient.parseDXF(fileBytes, file.getOriginalFilename());

        double cutLength = ((Number) result.get("cutLength")).doubleValue();
        int pierceCount = ((Number) result.get("pierceCount")).intValue();

        double minX = ((Number) result.get("minX")).doubleValue();
        double minY = ((Number) result.get("minY")).doubleValue();
        double maxX = ((Number) result.get("maxX")).doubleValue();
        double maxY = ((Number) result.get("maxY")).doubleValue();

        double width = maxX - minX;
        double height = maxY - minY;

        double finalWidth = width + 2 * SHEET_MARGIN_MM;
        double finalHeight = height + 2 * SHEET_MARGIN_MM;

        double area = (finalWidth / 1000) * (finalHeight / 1000);
        double thicknessMeters = thickness / 1000;
        double volume = area * thicknessMeters;
        double weight = volume * material.getDensity() * quantity;
        double materialCost = weight * material.getPricePerKg();

        MachineCutParameters cutParameters = machineCutParametersService
                .getCutParameters(machine.getId(), material.getId(), thickness);

        double machineRatePerMin = machine.getRatePerHour() / 60;
        double cutSpeed = cutParameters.getSpeed() * machine.getEfficiencyFactor();
        double cutTime = (cutLength / cutSpeed) * quantity;
        double pierceTime = ((pierceCount * cutParameters.getPierceTime()) / 60.0) * quantity;
        double totalLaserTime = cutTime + pierceTime;
        double cuttingCost = totalLaserTime * machineRatePerMin;

        double bendTime = 0;
        double bendingCost = 0;

        if (bends > 0) {
            double setupTime = 5;
            double singleBendTime = bends * 0.5 * quantity;

            bendTime = setupTime + singleBendTime;
            bendingCost = bendTime * 0.8;
        }

        double totalTime = totalLaserTime + bendTime;

        QuoteDxfItem item = QuoteDxfItem.builder()
                .quote(quote)
                .material(material)
                .fileName(file.getOriginalFilename())
                .dxfFile(fileBytes)
                .partName(partName)
                .quantity(quantity)
                .thickness(thickness)
                .bends(bends)
                .cutLength(cutLength)
                .pierceCount(pierceCount)
                .width(finalWidth)
                .height(finalHeight)
                .weight(weight)
                .materialCost(materialCost)
                .cutTime(cutTime)
                .pierceTime(pierceTime)
                .bendTime(bendTime)
                .totalTime(totalTime)
                .cuttingCost(cuttingCost)
                .bendingCost(bendingCost)
                .build();

        item.getOperations().addAll(buildItemOperations(item, operations, quantity, bends));
        applyItemPrice(item, margin);

        return item;
    }

    private void updateQuoteItem(Quote quote, UpdateQuoteItemRequest request) {
        if (request == null) {
            throw new APIException("Quote item update cannot be empty");
        }

        if (request.getItemId() == null) {
            throw new APIException("Quote item id is required");
        }

        QuoteDxfItem item = quote.getItems().stream()
                .filter(candidate -> request.getItemId().equals(candidate.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Quote DXF item", "id", request.getItemId()));

        if (request.getMaterialId() != null) {
            item.setMaterial(materialService.getById(request.getMaterialId()));
        }

        if (request.getBends() != null) {
            if (request.getBends() < 0) {
                throw new APIException("Bends cannot be negative");
            }
            item.setBends(request.getBends());
        }

        if (request.getQuantity() != null) {
            if (request.getQuantity() <= 0) {
                throw new APIException("Quantity must be greater than zero");
            }
            item.setQuantity(request.getQuantity());
        }

        if (request.getOperations() != null) {
            item.getOperations().clear();
            item.getOperations().addAll(buildItemOperations(item, request.getOperations(),
                    item.getQuantity() > 0 ? item.getQuantity() : 1, item.getBends()));
        } else if (request.getOperationIds() != null) {
            item.getOperations().clear();
            item.getOperations().addAll(buildItemOperations(item, toLegacyOperationRequests(request.getOperationIds()),
                    item.getQuantity() > 0 ? item.getQuantity() : 1, item.getBends()));
        }
    }

    private void recalculateItem(QuoteDxfItem item, Machine machine, int margin) {
        Material material = item.getMaterial();
        int quantity = item.getQuantity() > 0 ? item.getQuantity() : 1;

        double area = (item.getWidth() / 1000) * (item.getHeight() / 1000);
        double thicknessMeters = item.getThickness() / 1000;
        double volume = area * thicknessMeters;
        double weight = volume * material.getDensity() * quantity;
        double materialCost = weight * material.getPricePerKg();

        MachineCutParameters cutParameters = machineCutParametersService
                .getCutParameters(machine.getId(), material.getId(), item.getThickness());

        double machineRatePerMin = machine.getRatePerHour() / 60;
        double cutSpeed = cutParameters.getSpeed() * machine.getEfficiencyFactor();
        double cutTime = (item.getCutLength() / cutSpeed) * quantity;
        double pierceTime = ((item.getPierceCount() * cutParameters.getPierceTime()) / 60.0) * quantity;
        double totalLaserTime = cutTime + pierceTime;
        double cuttingCost = totalLaserTime * machineRatePerMin;

        double bendTime = 0;
        double bendingCost = 0;

        if (item.getBends() > 0) {
            double setupTime = 5;
            double singleBendTime = item.getBends() * 0.5 * quantity;

            bendTime = setupTime + singleBendTime;
            bendingCost = bendTime * 0.8;
        }

        double totalTime = totalLaserTime + bendTime;

        item.setWeight(weight);
        item.setMaterialCost(materialCost);
        item.setCutTime(cutTime);
        item.setPierceTime(pierceTime);
        item.setBendTime(bendTime);
        item.setTotalTime(totalTime);
        item.setCuttingCost(cuttingCost);
        item.setBendingCost(bendingCost);

        recalculateItemOperations(item, quantity, item.getBends());
        applyItemPrice(item, margin);
    }

    private void applyTotals(Quote quote) {
        double totalWeight = 0;
        double totalMaterialCost = 0;
        double totalTime = 0;
        double calculatedPrice = 0;
        double cost = 0;
        double cuttingCost = 0;
        double bendingCost = 0;
        double operationCost = 0;
        int totalQuantity = 0;

        for (QuoteDxfItem item : quote.getItems()) {
            totalWeight += item.getWeight();
            totalMaterialCost += item.getMaterialCost();
            totalTime += item.getTotalTime();
            calculatedPrice += item.getPrice();
            cost += item.getCost();
            cuttingCost += item.getCuttingCost();
            bendingCost += item.getBendingCost();
            operationCost += item.getOperationCost();
            totalQuantity += item.getQuantity() > 0 ? item.getQuantity() : 1;
        }
        boolean minCharged = quote.getMinimumCharge() > 0 && calculatedPrice < quote.getMinimumCharge();
        double defaultFinalPrice = minCharged ? quote.getMinimumCharge() : calculatedPrice;
        double finalPrice = quote.isFinalPriceOverridden() ? quote.getTotalPrice() : defaultFinalPrice;

        quote.setTotalWeight(totalWeight);
        quote.setTotalMaterialCost(totalMaterialCost);
        quote.setTotalTime(totalTime);
        quote.setCalculatedPrice(calculatedPrice);
        quote.setTotalPrice(finalPrice);
        quote.setTotalQuantity(totalQuantity);
        quote.setCost(cost);
        quote.setProfit(finalPrice - cost);
        quote.setCuttingCost(cuttingCost);
        quote.setBendingCost(bendingCost);
        quote.setOperationCost(operationCost);
        quote.setMinCharged(minCharged);
    }

    private QuoteDetailsResponse mapToDetailsResponse(Quote quote) {
        return QuoteDetailsResponse.builder()
                .id(quote.getId())
                .machineId(quote.getMachine().getId())
                .machineName(quote.getMachine().getName())
                .quoteName(quote.getQuoteName())
                .customerName(quote.getCustomerName())
                .status(resolveStatus(quote))
                .totalWeight(quote.getTotalWeight())
                .totalMaterialCost(quote.getTotalMaterialCost())
                .cuttingCost(quote.getCuttingCost())
                .bendingCost(quote.getBendingCost())
                .operationCost(quote.getOperationCost())
                .totalTime(quote.getTotalTime())
                .calculatedPrice(quote.getCalculatedPrice())
                .totalPrice(quote.getTotalPrice())
                .finalPriceOverridden(quote.isFinalPriceOverridden())
                .totalQuantity(quote.getTotalQuantity())
                .cost(quote.getCost())
                .profit(quote.getProfit())
                .margin(quote.getMargin())
                .minimumCharge(quote.getMinimumCharge())
                .minCharged(quote.isMinCharged())
                .createdAt(quote.getCreatedAt())
                .items(quote.getItems().stream()
                        .map(item -> mapToItemResponse(quote.getId(), item))
                        .toList())
                .build();
    }

    private QuoteListResponse2 mapToListResponse(Quote quote) {
        return QuoteListResponse2.builder()
                .id(quote.getId())
                .quoteName(quote.getQuoteName())
                .customerName(quote.getCustomerName())
                .status(resolveStatus(quote))
                .machineId(quote.getMachine().getId())
                .machineName(quote.getMachine().getName())
                .itemCount(quote.getItems().size())
                .totalQuantity(quote.getTotalQuantity())
                .totalWeight(quote.getTotalWeight())
                .totalMaterialCost(quote.getTotalMaterialCost())
                .operationCost(quote.getOperationCost())
                .totalTime(quote.getTotalTime())
                .calculatedPrice(quote.getCalculatedPrice())
                .totalPrice(quote.getTotalPrice())
                .finalPriceOverridden(quote.isFinalPriceOverridden())
                .minimumCharge(quote.getMinimumCharge())
                .minCharged(quote.isMinCharged())
                .createdAt(quote.getCreatedAt())
                .build();
    }

    private QuoteDxfItemResponse mapToItemResponse(Long quoteId, QuoteDxfItem item) {
        return QuoteDxfItemResponse.builder()
                .id(item.getId())
                .fileName(item.getFileName())
                .dxfDownloadUrl("/api/quote/" + quoteId + "/items/" + item.getId() + "/download")
                .materialId(item.getMaterial().getId())
                .materialName(item.getMaterial().getName())
                .materialPrice(item.getMaterial().getPricePerKg())
                .partName(item.getPartName())
                .quantity(item.getQuantity() > 0 ? item.getQuantity() : 1)
                .thickness(item.getThickness())
                .bends(item.getBends())
                .cutLength(item.getCutLength())
                .pierceCount(item.getPierceCount())
                .width(item.getWidth())
                .height(item.getHeight())
                .weight(item.getWeight())
                .materialCost(item.getMaterialCost())
                .cutTime(item.getCutTime())
                .pierceTime(item.getPierceTime())
                .bendTime(item.getBendTime())
                .totalTime(item.getTotalTime())
                .price(item.getPrice())
                .cost(item.getCost())
                .profit(item.getProfit())
                .cuttingCost(item.getCuttingCost())
                .bendingCost(item.getBendingCost())
                .operationCost(item.getOperationCost())
                .operations(item.getOperations().stream()
                        .map(this::mapItemOperationToResponse)
                        .toList())
                .build();
    }

    private QuoteItemOperationResponse mapItemOperationToResponse(QuoteDxfItemOperation itemOperation) {
        Operation operation = itemOperation.getOperation();

        return QuoteItemOperationResponse.builder()
                .id(itemOperation.getId())
                .operationId(operation.getId())
                .name(operation.getName())
                .pricingMode(operation.getPricingMode())
                .rate(operation.getRate())
                .timeMinutes(itemOperation.getTimeMinutes())
                .cost(itemOperation.getCost())
                .active(operation.isActive())
                .build();
    }

    private String resolveSortBy(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "createdAt";
        }

        return switch (sortBy) {
            case "id",
                 "quoteName",
                 "customerName",
                 "status",
                 "totalQuantity",
                 "totalWeight",
                 "totalMaterialCost",
                 "operationCost",
                 "totalTime",
                 "calculatedPrice",
                 "totalPrice",
                 "createdAt" -> sortBy;
            case "weight" -> "totalWeight";
            case "price" -> "totalPrice";
            default -> "createdAt";
        };
    }

    private void validateCreateQuoteRequest(CreateQuoteRequest request) {
        if (request == null) {
            throw new APIException("Quote request is required");
        }

        if (request.getQuoteName() == null || request.getQuoteName().isBlank()) {
            throw new APIException("Quote name is required");
        }

        if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
            throw new APIException("Customer name is required");
        }

        if (request.getMaterialIds() == null || request.getMaterialIds().isEmpty()) {
            throw new APIException("Materials are required");
        }

        if (request.getMachineId() == null) {
            throw new APIException("Machine is required");
        }

        if (request.getFiles() == null || request.getFiles().isEmpty()) {
            throw new APIException("At least one DXF file is required");
        }

        int itemCount = request.getFiles().size();
        validateListSize(request.getPartNames(), itemCount, "Part names");
        validateOptionalListSize(request.getQuantities(), itemCount, "Quantities");
        validateListSize(request.getThicknesses(), itemCount, "Thicknesses");
        validateListSize(request.getBends(), itemCount, "Bends");
        validateListSize(request.getMaterialIds(), itemCount, "Materials");
//        validateOptionalListSize(request.getOperationIds(), itemCount, "Operations");
//        validateOptionalListSize(request.getOperations(), itemCount, "Operations");

        if (request.getMinimumCharge() < 0) {
            throw new APIException("Minimum charge cannot be negative");
        }

        for (int i = 0; i < itemCount; i++) {
            MultipartFile file = request.getFiles().get(i);

            if (file == null || file.isEmpty()) {
                throw new APIException("DXF file is required for item " + (i + 1));
            }

            if (request.getPartNames().get(i) == null || request.getPartNames().get(i).isBlank()) {
                throw new APIException("Part name is required for item " + (i + 1));
            }

            if (request.getQuantities() != null &&
                    (request.getQuantities().get(i) == null || request.getQuantities().get(i) <= 0)) {
                throw new APIException("Quantity must be greater than zero for item " + (i + 1));
            }

            if (request.getThicknesses().get(i) == null || request.getThicknesses().get(i) <= 0) {
                throw new APIException("Thickness must be greater than zero for item " + (i + 1));
            }

            if (request.getBends().get(i) == null || request.getBends().get(i) < 0) {
                throw new APIException("Bends cannot be negative for item " + (i + 1));
            }

            if (request.getMaterialIds().get(i) == null) {
                throw new APIException("Material is required for item " + (i + 1));
            }
        }
    }

    private void validateListSize(List<?> values, int expectedSize, String fieldName) {
        if (values == null || values.size() != expectedSize) {
            throw new APIException(fieldName + " must match the number of DXF files");
        }
    }

    private void validateOptionalListSize(List<?> values, int expectedSize, String fieldName) {
        if (values != null && values.size() != expectedSize) {
            throw new APIException(fieldName + " must match the number of DXF files");
        }
    }

    private int resolveQuantity(CreateQuoteRequest request, int index) {
        if (request.getQuantities() == null) {
            return 1;
        }

        return request.getQuantities().get(index);
    }

    private List<QuoteItemOperationRequest> resolveOperationRequests(CreateQuoteRequest request, int index) {
        if (request.getOperations() != null) {
            return parseOperationRequests(request.getOperations().get(index));
        }

        if (request.getOperationIds() == null) {
            return new ArrayList<>();
        }

        String operationIds = request.getOperationIds().get(index);
        if (operationIds == null || operationIds.isBlank()) {
            return new ArrayList<>();
        }

        return parseLegacyOperationIds(operationIds);
    }

    private List<QuoteItemOperationRequest> parseOperationRequests(String rawOperations) {
        if (rawOperations == null || rawOperations.isBlank()) {
            return new ArrayList<>();
        }

        String trimmed = rawOperations.trim();
        if (trimmed.startsWith("[")) {
            try {
                return objectMapper.readValue(trimmed, new TypeReference<List<QuoteItemOperationRequest>>() {
                });
            } catch (Exception ex) {
                throw new APIException("Operations must be valid JSON");
            }
        }

        return parseLegacyOperationIds(trimmed);
    }

    private List<QuoteItemOperationRequest> parseLegacyOperationIds(String operationIds) {
        try {
            return Arrays.stream(operationIds.split(","))
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .map(this::parseOperationToken)
                    .toList();
        } catch (NumberFormatException ex) {
            throw new APIException("Operation ids must be valid numbers");
        }
    }

    private QuoteItemOperationRequest parseOperationToken(String token) {
        String[] values = token.split(":");
        if (values.length > 2) {
            throw new APIException("Operation format must be operationId or operationId:timeMinutes");
        }

        Long operationId = Long.valueOf(values[0].trim());
        Double timeMinutes = values.length > 1 && !values[1].isBlank()
                ? Double.valueOf(values[1].trim())
                : null;

        return QuoteItemOperationRequest.builder()
                .operationId(operationId)
                .timeMinutes(timeMinutes)
                .build();
    }

    private List<QuoteItemOperationRequest> toLegacyOperationRequests(List<Long> operationIds) {
        if (operationIds == null || operationIds.isEmpty()) {
            return new ArrayList<>();
        }

        return operationIds.stream()
                .map(operationId -> QuoteItemOperationRequest.builder()
                        .operationId(operationId)
                        .build())
                .toList();
    }

    private List<QuoteDxfItemOperation> buildItemOperations(
            QuoteDxfItem item,
            List<QuoteItemOperationRequest> requests,
            int quantity,
            int bends
    ) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(request -> buildItemOperation(item, request, quantity, bends))
                .toList();
    }

    private QuoteDxfItemOperation buildItemOperation(
            QuoteDxfItem item,
            QuoteItemOperationRequest request,
            int quantity,
            int bends
    ) {
        if (request == null || request.getOperationId() == null) {
            throw new APIException("Operation id is required");
        }

        Operation operation = operationService.getById(request.getOperationId());
        validateOperationIsActive(operation);

        double timeMinutes = resolveOperationTime(operation, request.getTimeMinutes());
        double cost = calculateOperationCost(operation, quantity, bends, timeMinutes);

        return QuoteDxfItemOperation.builder()
                .quoteDxfItem(item)
                .operation(operation)
                .timeMinutes(timeMinutes)
                .cost(cost)
                .build();
    }

    private void recalculateItemOperations(QuoteDxfItem item, int quantity, int bends) {
        for (QuoteDxfItemOperation itemOperation : item.getOperations()) {
            itemOperation.setCost(calculateOperationCost(
                    itemOperation.getOperation(),
                    quantity,
                    bends,
                    itemOperation.getTimeMinutes()
            ));
        }
    }

    private void applyItemPrice(QuoteDxfItem item, int margin) {
        double operationCost = item.getOperations().stream()
                .mapToDouble(QuoteDxfItemOperation::getCost)
                .sum();
        double cost = item.getMaterialCost() + item.getCuttingCost() + item.getBendingCost() + operationCost;
        double profit = cost * (margin / 100.0);

        item.setOperationCost(operationCost);
        item.setCost(cost);
        item.setProfit(profit);
        item.setPrice(cost + profit);
    }

    private void validateOperationIsActive(Operation operation) {
        if (!operation.isActive()) {
            throw new APIException("Operation is not active: " + operation.getName());
        }
    }

    private double resolveOperationTime(Operation operation, Double requestedTimeMinutes) {
        if (operation.getPricingMode() == null) {
            throw new APIException("Pricing mode is required for operation: " + operation.getName());
        }

        if (operation.getPricingMode().requiresTime()) {
            if (requestedTimeMinutes == null || requestedTimeMinutes <= 0) {
                throw new APIException("Time in minutes is required for operation: " + operation.getName());
            }

            return requestedTimeMinutes;
        }

        if (requestedTimeMinutes != null && requestedTimeMinutes < 0) {
            throw new APIException("Operation time cannot be negative");
        }

        return requestedTimeMinutes != null ? requestedTimeMinutes : 0;
    }

    private double calculateOperationCost(Operation operation, int quantity, int bends, double timeMinutes) {
        if (operation.getPricingMode() == null) {
            throw new APIException("Pricing mode is required for operation: " + operation.getName());
        }

        return switch (operation.getPricingMode()) {
            case FIXED, PER_ITEM -> operation.getRate();
            case PER_PART, PER_QUANTITY -> operation.getRate() * quantity;
            case PER_MINUTE -> operation.getRate() * timeMinutes;
            case PER_HOUR -> operation.getRate() * (timeMinutes / 60.0);
            case PER_BEND -> operation.getRate() * bends * quantity;
        };
    }

    private double resolveMinimumCharge(double requestedMinimumCharge, Machine machine) {
        return requestedMinimumCharge > 0 ? requestedMinimumCharge : machine.getMinimumCharge();
    }

    private boolean isDraft(Quote quote) {
        return resolveStatus(quote) == QuoteStatus.DRAFT;
    }

    private QuoteStatus resolveStatus(Quote quote) {
        return quote.getStatus() != null ? quote.getStatus() : QuoteStatus.DRAFT;
    }

    private void validateStatusTransition(QuoteStatus currentStatus, QuoteStatus nextStatus) {
        if (currentStatus == nextStatus) {
            return;
        }

        if (currentStatus == QuoteStatus.DRAFT && nextStatus == QuoteStatus.SENT) {
            return;
        }

        if (currentStatus == QuoteStatus.SENT && nextStatus == QuoteStatus.ACCEPTED) {
            return;
        }

        throw new APIException("Quote status cannot move from " + currentStatus + " to " + nextStatus);
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new APIException(fieldName + " is required");
        }
    }
}
