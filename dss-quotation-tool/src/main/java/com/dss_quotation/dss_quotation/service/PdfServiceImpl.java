package com.dss_quotation.dss_quotation.service;

import com.dss_quotation.dss_quotation.exceptions.APIException;
import com.dss_quotation.dss_quotation.models.Quote;
import com.dss_quotation.dss_quotation.models.QuoteDxfItem;
import com.dss_quotation.dss_quotation.models.QuoteDxfItemOperation;
import com.dss_quotation.dss_quotation.payload.QuotePdfDTO;
import com.dss_quotation.dss_quotation.payload.QuotePdfItemDTO;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final QuoteService quoteService;
    private final TemplateEngine templateEngine;

    @Override
    @Transactional(readOnly = true)
    public byte[] exportCustomerOffer(Long quoteId) {
        Quote quote = quoteService.getById(quoteId);
        QuotePdfDTO dto = mapToPdfDTO(quote, true);

        return renderPdf("CustomerOfferTemplate", dto);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportQuoteDetails(Long quoteId) {
        Quote quote = quoteService.getById(quoteId);
        QuotePdfDTO dto = mapToPdfDTO(quote, false);

        return renderPdf("QuoteDetailsTemplate", dto);
    }

    private byte[] renderPdf(String templateName, QuotePdfDTO dto) {
        Context context = new Context();
        context.setVariable("quote", dto);

        String html = templateEngine.process(templateName, context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new APIException("Failed to export quote PDF: " + e.getMessage());
        }
    }

    private QuotePdfDTO mapToPdfDTO(Quote quote, boolean customerOffer) {
        List<QuotePdfItemDTO> items = customerOffer
                ? mapToCustomerOfferItemDTOs(quote)
                : quote.getItems().stream().map(this::mapToQuoteDetailsItemDTO).toList();
        BigDecimal finalPrice = money(quote.getTotalPrice());

        LocalDate quoteDate = quote.getCreatedAt() != null
                ? quote.getCreatedAt().toLocalDate()
                : LocalDate.now();

        return QuotePdfDTO.builder()
                .id(quote.getId())
                .quoteNumber(String.valueOf(quote.getId()))
                .quoteName(quote.getQuoteName())
                .customerName(quote.getCustomerName())
                .customerAddress("")
                .customerEmail("")
                .quoteDate(DATE_FORMATTER.format(quoteDate))
                .dueDate(DATE_FORMATTER.format(quoteDate.plusDays(14)))
                .validUntil(DATE_FORMATTER.format(quoteDate.plusDays(30)))
                .machineName(quote.getMachine().getName())
                .status(quote.getStatus() != null ? quote.getStatus().name() : "DRAFT")
                .totalQuantity(quote.getTotalQuantity())
                .items(items)
                .subtotal(formatMoney(finalPrice))
                .calculatedPrice(formatMoney(quote.getCalculatedPrice()))
                .minimumCharge(formatMoney(quote.getMinimumCharge()))
                .minCharged(quote.isMinCharged())
                .finalPriceOverridden(quote.isFinalPriceOverridden())
                .tax(formatMoney(0))
                .total(formatMoney(finalPrice))
                .materialCost(formatMoney(quote.getTotalMaterialCost()))
                .cuttingCost(formatMoney(quote.getCuttingCost()))
                .bendingCost(formatMoney(quote.getBendingCost()))
                .operationCost(formatMoney(quote.getOperationCost()))
                .totalCost(formatMoney(quote.getCost()))
                .profit(formatMoney(quote.getProfit()))
                .margin(quote.getMargin())
                .totalWeight(formatNumber(quote.getTotalWeight()))
                .totalTime(formatNumber(quote.getTotalTime()))
                .build();
    }

    private List<QuotePdfItemDTO> mapToCustomerOfferItemDTOs(Quote quote) {
        List<QuoteDxfItem> quoteItems = quote.getItems();
        BigDecimal finalPrice = money(quote.getTotalPrice());
        BigDecimal calculatedPrice = quote.getCalculatedPrice() > 0
                ? money(quote.getCalculatedPrice())
                : quoteItems.stream()
                .map(item -> money(item.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal assignedTotal = BigDecimal.ZERO;
        List<QuotePdfItemDTO> items = new java.util.ArrayList<>();

        for (int i = 0; i < quoteItems.size(); i++) {
            QuoteDxfItem item = quoteItems.get(i);
            BigDecimal amount;

            if (i == quoteItems.size() - 1) {
                amount = finalPrice.subtract(assignedTotal);
            } else if (calculatedPrice.compareTo(BigDecimal.ZERO) > 0) {
                amount = money(item.getPrice())
                        .multiply(finalPrice)
                        .divide(calculatedPrice, 2, RoundingMode.HALF_UP);
                assignedTotal = assignedTotal.add(amount);
            } else {
                amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }

            items.add(mapToCustomerOfferItemDTO(item, amount));
        }

        return items;
    }

    private QuotePdfItemDTO mapToCustomerOfferItemDTO(QuoteDxfItem item, BigDecimal amount) {
        int quantity = item.getQuantity() > 0 ? item.getQuantity() : 1;
        BigDecimal unitPrice = amount.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP);

        return QuotePdfItemDTO.builder()
                .qty(quantity)
                .quantity(quantity)
                .partName(item.getPartName())
                .materialName(item.getMaterial().getName())
                .thickness(formatNumber(item.getThickness()))
                .description(buildDescription(item))
                .unitPrice(formatMoney(unitPrice))
                .amount(formatMoney(amount))
                .build();
    }

    private QuotePdfItemDTO mapToQuoteDetailsItemDTO(QuoteDxfItem item) {
        int quantity = item.getQuantity() > 0 ? item.getQuantity() : 1;
        BigDecimal amount = money(item.getPrice());
        BigDecimal unitPrice = amount.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP);

        return QuotePdfItemDTO.builder()
                .qty(quantity)
                .quantity(quantity)
                .partName(item.getPartName())
                .materialName(item.getMaterial().getName())
                .thickness(formatNumber(item.getThickness()))
                .description(buildDescription(item))
                .unitPrice(formatMoney(unitPrice))
                .amount(formatMoney(amount))
                .calculatedAmount(formatMoney(amount))
                .materialCost(formatMoney(item.getMaterialCost()))
                .cuttingCost(formatMoney(item.getCuttingCost()))
                .bendingCost(formatMoney(item.getBendingCost()))
                .operationCost(formatMoney(item.getOperationCost()))
                .operations(formatOperations(item.getOperations()))
                .weight(formatNumber(item.getWeight()))
                .cutTime(formatNumber(item.getCutTime()))
                .pierceTime(formatNumber(item.getPierceTime()))
                .bendTime(formatNumber(item.getBendTime()))
                .totalTime(formatNumber(item.getTotalTime()))
                .bends(item.getBends())
                .pierceCount(item.getPierceCount())
                .cutLength(formatNumber(item.getCutLength()))
                .build();
    }

    private String buildDescription(QuoteDxfItem item) {
        return item.getPartName()
                + " | " + item.getMaterial().getName()
                + " | " + formatNumber(item.getThickness()) + "mm"
                + " | bends: " + item.getBends();
    }

    private String formatOperations(List<QuoteDxfItemOperation> operations) {
        if (operations == null || operations.isEmpty()) {
            return "-";
        }

        return operations.stream()
                .map(itemOperation -> itemOperation.getOperation().getName()
                        + " (" + formatNumber(itemOperation.getTimeMinutes()) + " min)" + " - €" + String.format("%.2f", itemOperation.getCost())
                        )
                .reduce((first, second) -> first + ", " + second)
                .orElse("-");
    }

    private BigDecimal money(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    private String formatMoney(double value) {
        return formatMoney(money(value));
    }

    private String formatMoney(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String formatNumber(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }
}
