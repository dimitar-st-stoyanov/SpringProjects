package com.example.dss_hellbomreader.service;

import com.example.dss_hellbomreader.model.AggregatedRow;
import com.example.dss_hellbomreader.model.CsvRow;
import com.example.dss_hellbomreader.util.QuantityParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CsvService {

    private static final char[] POSSIBLE_SEPARATORS = {',', ';', '|', '\t'};

    /**
     * Detect CSV separator by counting occurrences in the header line.
     */
    private char detectSeparator(String headerLine) {
        char best = ',';
        int bestCount = 0;
        for (char sep : POSSIBLE_SEPARATORS) {
            int count = headerLine.length() - headerLine.replace(String.valueOf(sep), "").length();
            if (count > bestCount) {
                best = sep;
                bestCount = count;
            }
        }
        return best;
    }

    /**
     * Reads CSV file into list of CsvRow using header names.
     * Empty fields are filled with "".
     */
    public List<CsvRow> readCsv(String filePath) throws Exception {
        List<CsvRow> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {

            String headerLine = reader.readLine();
            if (headerLine == null) return rows;

            char separator = detectSeparator(headerLine);

            String[] headers = headerLine.split(String.valueOf(separator), -1);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim(), i);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] parts = line.split(String.valueOf(separator), -1);
                CsvRow row = new CsvRow();

                row.setLevel(getField(parts, headerMap, "Level"));
                row.setPos(getField(parts, headerMap, "Pos."));
                row.setQuantity(getField(parts, headerMap, "Quantity"));
                row.setUnit(getField(parts, headerMap, "Unit"));
                row.setComponent(getField(parts, headerMap, "Component"));
                row.setTextOfPos(getField(parts, headerMap, "Text of pos."));
                row.setTextOfPos2(getField(parts, headerMap, "Text of pos. 2"));
                row.setItemCa(getField(parts, headerMap, "ItemCa"));
                row.setBom(getField(parts, headerMap, "BOM"));
                row.setProvInd(getField(parts, headerMap, "Prov.ind."));
                row.setProdRel(getField(parts, headerMap, "Prod.rel."));
                row.setValidFrom(getField(parts, headerMap, "Valid from"));
                row.setValidTo(getField(parts, headerMap, "Valid to"));
                row.setPlantSpStatu(getField(parts, headerMap, "Plant-sp. statu"));
                row.setXPlantStatus(getField(parts, headerMap, "X-plant status"));
                row.setDocType(getField(parts, headerMap, "Doc.type"));
                row.setDocNo(getField(parts, headerMap, "Doc. no."));
                row.setDocVersio(getField(parts, headerMap, "Doc.versio"));
                row.setDocPart(getField(parts, headerMap, "Doc.part"));
                row.setDocStatus(getField(parts, headerMap, "Doc.status"));
                row.setSubitemText(getField(parts, headerMap, "Subitem text"));
                row.setColor(getField(parts, headerMap, "Color"));
                row.setLongText1(getField(parts, headerMap, "Long text 1"));
                row.setLongText2(getField(parts, headerMap, "Long text 2"));
                row.setLongText3(getField(parts, headerMap, "Long text 3"));
                row.setLongText4(getField(parts, headerMap, "Long text 4"));
                row.setLongText5(getField(parts, headerMap, "Long text 5"));
                row.setLongText6(getField(parts, headerMap, "Long text 6"));

                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * Helper to safely get a field by header name, defaults to "" if missing
     */
    private String getField(String[] parts, Map<String, Integer> headerMap, String fieldName) {
        Integer index = headerMap.get(fieldName);
        if (index == null || index >= parts.length) return "";
        return parts[index].trim();
    }

    /**
     * Aggregates rows by Component, summing Quantity.
     * Copies other fields from the first occurrence.
     */
    public List<AggregatedRow> aggregateRows(List<CsvRow> rows) {
        Map<String, AggregatedRow> map = new LinkedHashMap<>();

        for (CsvRow r : rows) {
            String comp = r.getComponent();
            double qty = QuantityParser.parseSafe(r.getQuantity(), r.getUnit());

            if (!map.containsKey(comp)) {
                AggregatedRow agg = new AggregatedRow();
                agg.setComponent(comp);
                agg.setQuantity(qty);
                agg.setUnit(r.getUnit());
                agg.setTextOfPos(r.getTextOfPos());
                agg.setTextOfPos2(r.getTextOfPos2());
                agg.setLevel(r.getLevel());
                agg.setPos(r.getPos());
                agg.setItemCa(r.getItemCa());
                agg.setBom(r.getBom());
                agg.setProvInd(r.getProvInd());
                agg.setProdRel(r.getProdRel());
                agg.setValidFrom(r.getValidFrom());
                agg.setValidTo(r.getValidTo());
                agg.setPlantSpStatu(r.getPlantSpStatu());
                agg.setXPlantStatus(r.getXPlantStatus());
                agg.setDocType(r.getDocType());
                agg.setDocNo(r.getDocNo());
                agg.setDocVersio(r.getDocVersio());
                agg.setDocPart(r.getDocPart());
                agg.setDocStatus(r.getDocStatus());
                agg.setSubitemText(r.getSubitemText());
                agg.setColor(r.getColor());
                agg.setLongText1(r.getLongText1());
                agg.setLongText2(r.getLongText2());
                agg.setLongText3(r.getLongText3());
                agg.setLongText4(r.getLongText4());
                agg.setLongText5(r.getLongText5());
                agg.setLongText6(r.getLongText6());

                map.put(comp, agg);
            } else {
                AggregatedRow agg = map.get(comp);
                agg.setQuantity(agg.getQuantity() + qty);
            }
        }

        return new ArrayList<>(map.values());
    }

    /**
     * Export aggregated rows to CSV with comma separator.
     * All fields are quoted to handle commas inside text.
     */
    public void exportCsv(List<AggregatedRow> rows, String filePath) throws Exception {
        char separator = ',';

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            // Write header
            String[] headers = {
                    "Level","Pos.","Quantity","Unit","Component","Text of pos.","Text of pos. 2",
                    "ItemCa","BOM","Prov.ind.","Prod.rel.","Valid from","Valid to","Plant-sp. statu",
                    "X-plant status","Doc.type","Doc. no.","Doc.versio","Doc.part","Doc.status",
                    "Subitem text","Color","Long text 1","Long text 2","Long text 3","Long text 4",
                    "Long text 5","Long text 6"
            };
            writeLine(writer, headers, separator);

            // Write rows
            for (AggregatedRow r : rows) {
                String[] line = {
                        r.getLevel(), r.getPos(), String.valueOf(r.getQuantity()), r.getUnit(), r.getComponent(),
                        r.getTextOfPos(), r.getTextOfPos2(), r.getItemCa(), r.getBom(), r.getProvInd(),
                        r.getProdRel(), r.getValidFrom(), r.getValidTo(), r.getPlantSpStatu(),
                        r.getXPlantStatus(), r.getDocType(), r.getDocNo(), r.getDocVersio(), r.getDocPart(),
                        r.getDocStatus(), r.getSubitemText(), r.getColor(), r.getLongText1(), r.getLongText2(),
                        r.getLongText3(), r.getLongText4(), r.getLongText5(), r.getLongText6()
                };
                writeLine(writer, line, separator);
            }
        }
    }

    /**
     * Writes a CSV line with all values quoted
     */
    private void writeLine(BufferedWriter writer, String[] values, char separator) throws IOException {
        for (int i = 0; i < values.length; i++) {
            String v = values[i] != null ? values[i] : "";
            writer.write("\"" + v.replace("\"", "\"\"") + "\"");
            if (i < values.length - 1) writer.write(separator);
        }
        writer.newLine();
    }
}
