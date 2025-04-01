package com.raison.gbr.Service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.raison.gbr.Repository.*;
import com.raison.gbr.entity.BaseSilo;
import org.apache.poi.ss.usermodel.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SiloService {

    @Autowired private Silo1Repository silo1Repository;
    @Autowired private Silo2Repository silo2Repository;
    @Autowired private Silo3Repository silo3Repository;
    @Autowired private Silo4Repository silo4Repository;
    @Autowired private Silo5Repository silo5Repository;
    @Autowired private Silo6Repository silo6Repository;
    @Autowired private Silo7Repository silo7Repository;
    @Autowired private Silo8Repository silo8Repository;

    public Map<String, List<Map<String, Object>>> getSiloData(String siloType, String reportType, LocalDateTime start, LocalDateTime end) {
        List<List<? extends BaseSilo>> siloData = new ArrayList<>();

        if ("Meal Silo".equalsIgnoreCase(siloType)) {
            siloData = List.of(
                silo1Repository.findByDateTimeBetween(start, end),
                silo2Repository.findByDateTimeBetween(start, end),
                silo3Repository.findByDateTimeBetween(start, end),
                silo4Repository.findByDateTimeBetween(start, end)
            );
        } else if ("Bulk Silo".equalsIgnoreCase(siloType)) {
            siloData = List.of(
                silo5Repository.findByDateTimeBetween(start, end),
                silo6Repository.findByDateTimeBetween(start, end),
                silo7Repository.findByDateTimeBetween(start, end),
                silo8Repository.findByDateTimeBetween(start, end)
            );
        }

        // Convert to filtered list of maps
        Map<String, List<Map<String, Object>>> result = new LinkedHashMap<>();
        int index = 1;
        for (List<? extends BaseSilo> siloList : siloData) {
            List<Map<String, Object>> filteredList = siloList.stream()
                .map(silo -> filterColumns(silo, reportType))
                .collect(Collectors.toList());
            result.put("Silo" + index, filteredList);
            index++;
        }

        return result;
    }

    private Map<String, Object> filterColumns(BaseSilo silo, String reportType) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("DateTime", silo.getDateTime());
        result.put("SiloName", silo.getSiloName());
        result.put("MaterialName", silo.getMaterialName());
        result.put("ActualWeight", silo.getActualWeight());
        result.put("Intake", silo.getIntake());
        result.put("DestinationBin", silo.getDestinationBin());

        if (!"Movement Report".equalsIgnoreCase(reportType)) {
            result.put("WeightAdded", silo.getWeightAdded());
        }
        if (!"Received Report".equalsIgnoreCase(reportType)) {
            result.put("WeightTaken", silo.getWeightTaken());
        }
        return result;
    }

    
    public byte[] exportSiloDataToExcel(String siloType, String reportType, LocalDateTime start, LocalDateTime end) throws IOException {
        Map<String, List<Map<String, Object>>> siloData = getSiloData(siloType, reportType, start, end);
    
        try (Workbook workbook = new XSSFWorkbook()) {
            for (Map.Entry<String, List<Map<String, Object>>> entry : siloData.entrySet()) {
                String sheetName = entry.getKey();
                List<Map<String, Object>> records = entry.getValue();
    
                Sheet sheet = workbook.createSheet(sheetName);
                if (!records.isEmpty()) {
                    // Create header row with styling
                    Row headerRow = sheet.createRow(0);
                    List<String> headers = new ArrayList<>(records.get(0).keySet());
                    headers.add(0, "S.No"); // Add Serial Number column at the beginning
    
                    CellStyle headerStyle = getHeaderCellStyle(workbook);
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(headers.get(i));
                        cell.setCellStyle(headerStyle);
                    }
    
                    // Define Serial Number Cell Style (Center-Aligned)
                    CellStyle serialNumberStyle = workbook.createCellStyle();
                    serialNumberStyle.setAlignment(HorizontalAlignment.CENTER);
    
                    // Fill data rows with Serial Number
                    int rowIndex = 1;
                    for (int i = 0; i < records.size(); i++) {
                        Row row = sheet.createRow(rowIndex++);
                        int colIndex = 0;
    
                        // Add Serial Number (S.No) with Center Alignment
                        Cell serialCell = row.createCell(colIndex++);
                        serialCell.setCellValue(i + 1);
                        serialCell.setCellStyle(serialNumberStyle);
    
                        // Add remaining data
                        Map<String, Object> record = records.get(i);
                        for (String key : headers.subList(1, headers.size())) { // Skip "S.No"
                            Cell cell = row.createCell(colIndex++);
                            Object value = record.get(key);
    
                            if (value instanceof LocalDateTime) {
                                cell.setCellValue(formatDateTime((LocalDateTime) value)); // Remove seconds
                            } else if (value instanceof Number) {
                                cell.setCellValue(((Number) value).doubleValue());
                            } else {
                                cell.setCellValue(value != null ? value.toString() : "");
                            }
                        }
                    }
    
                    // Add total reports count row
                    Row totalRow = sheet.createRow(rowIndex);
                    Cell totalCell = totalRow.createCell(0);
                    totalCell.setCellValue("Total Reports: " + records.size());
                    totalCell.setCellStyle(getTotalCellStyle(workbook));
    
                    // Auto-size columns for better readability
                    for (int i = 0; i < headers.size(); i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
            }
    
            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    
    // Method to style header row (bold, blue background, white text)
    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        return headerStyle;
    }
    
    // Method to style total row
    private CellStyle getTotalCellStyle(Workbook workbook) {
        CellStyle totalStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        totalStyle.setFont(font);
        return totalStyle;
    }
    
    // Format DateTime without seconds
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }
    
}
