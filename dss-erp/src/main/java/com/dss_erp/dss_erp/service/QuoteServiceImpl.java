package com.dss_erp.dss_erp.service;

import com.dss_erp.dss_erp.models.Machine;
import com.dss_erp.dss_erp.models.Quote;
import com.dss_erp.dss_erp.payload.QuoteListResponse;
import com.dss_erp.dss_erp.payload.QuoteResponse;
import com.dss_erp.dss_erp.repositories.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuoteServiceImpl implements QuoteService {

    private final DxfParserClient dxfParserClient;
    private final MachineService machineService;
    private final MachineCutSpeedService machineCutSpeedService;
    private final QuoteRepository quoteRepository;


    @Override
    public QuoteResponse generateQuote(MultipartFile file,
                                       double thickness,
                                       int bends,
                                       String material,
                                       Long machineId) {

    /* ===============================
       1. CALL PYTHON
    =============================== */
        Map<String, Object> result = dxfParserClient.parseDXF(file);

    /* ===============================
       2. GEOMETRY
    =============================== */
        double cutLength = ((Number) result.get("cutLength")).doubleValue();
        int pierceCount = ((Number) result.get("pierceCount")).intValue();

        double minX = ((Number) result.get("minX")).doubleValue();
        double minY = ((Number) result.get("minY")).doubleValue();
        double maxX = ((Number) result.get("maxX")).doubleValue();
        double maxY = ((Number) result.get("maxY")).doubleValue();

        double width = maxX - minX;
        double height = maxY - minY;

        double margin = 20;
        double finalWidth = width + 2 * margin;
        double finalHeight = height + 2 * margin;

    /* ===============================
       3. MATERIAL
    =============================== */
        double area = (finalWidth / 1000) * (finalHeight / 1000);
        double thicknessMeters = thickness / 1000;

        double density = getDensity(material);

        double volume = area * thicknessMeters;
        double weight = volume * density;

        double pricePerKg = getMaterialPrice(material);
        double materialCost = weight * pricePerKg;

    /* ===============================
       4. LASER
    =============================== */
        Machine machine = machineService.getById(machineId);

        double machineRatePerMin = machine.getRatePerHour() / 60;

        double cutSpeed = machineCutSpeedService.getCutSpeed(machine.getId(), thickness);
        cutSpeed *= 0.8;

        double cutTime = cutLength / cutSpeed;

        double pierceTimeSeconds = getPierceTimeSeconds(thickness);
        double pierceTime = (pierceCount * pierceTimeSeconds) / 60.0;

        double totalLaserTime = cutTime + pierceTime;

    /* ===============================
       5. BENDING
    =============================== */
        double totalBendTime = 0;
        double bendCost = 0;

        if (bends > 0) {
            double setupTime = 5;
            double bendTime = bends * 0.5;

            totalBendTime = setupTime + bendTime;
            bendCost = totalBendTime * 0.8;
        }

    /* ===============================
       6. TOTAL
    =============================== */
        double totalTime = totalLaserTime + totalBendTime;

        double price =
                materialCost +
                        (totalLaserTime * machineRatePerMin) +
                        bendCost;

    /* ===============================
       7. SAVE QUOTE ✅
    =============================== */
        Quote quote = Quote.builder()
                .machine(machine)
                .material(material)
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
                .bendTime(totalBendTime)
                .totalTime(totalTime)
                .price(price)
                .createdAt(LocalDateTime.now())
                .build();

        quoteRepository.save(quote);

        return new QuoteResponse(
                cutLength,
                pierceCount,
                finalWidth,
                finalHeight,
                weight,
                materialCost,
                cutTime,
                pierceTime,
                totalBendTime,
                totalTime,
                price
        );
    }

    /* ===============================
       HELPER METHODS
    =============================== */

    private double getDensity(String material) {
        switch (material.toLowerCase()) {
            case "steel": return 7850;
            case "stainless": return 8000;
            case "aluminum": return 2700;
            default: return 7850;
        }
    }

    private double getMaterialPrice(String material) {
        switch (material.toLowerCase()) {
            case "steel": return 1.2;
            case "stainless": return 2.5;
            case "aluminum": return 3.0;
            default: return 1.5;
        }
    }

    // ✅ 6kW SPEED TABLE (mm/min)
    private double getCutSpeed(double thickness, double power) {

        // For now: only 6kW logic
        if (power == 6) {
            if (thickness <= 1) return 21500;
            if (thickness <= 1.5) return 15000;
            if (thickness <= 2) return 10000;
            if (thickness <= 2.5) return 7500;
            if (thickness <= 3) return 6000;
            if (thickness <= 4) return 4000;
            if (thickness <= 5) return 3250;
            if (thickness <= 6) return 2500;
            if (thickness <= 8) return 2000;
            if (thickness <= 10) return 1400;
        }

        return 1000; // fallback
    }

    private double getPierceTimeSeconds(double thickness) {

        if (thickness <= 1) return 0.2;
        if (thickness <= 2) return 0.3;
        if (thickness <= 3) return 0.5;
        if (thickness <= 5) return 0.8;
        if (thickness <= 8) return 1.2;
        if (thickness <= 10) return 1.8;

        return 2.5; // fallback for thick material
    }

    @Override
    public List<Quote> getAll() {
        return quoteRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Page<Quote> getPaginated(int page, int size) {
        return quoteRepository.findAll(
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );
    }
}