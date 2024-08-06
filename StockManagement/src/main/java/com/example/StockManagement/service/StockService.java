package com.example.StockManagement.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class StockService {

    public ByteArrayInputStream exportStockToExcel(Long marketId) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock Data");

            // Row for the market name
            Row marketNameRow = sheet.createRow(0);
            marketNameRow.createCell(0).setCellValue("Stock Data for: " + getMarketName(marketId));

            // Add an empty row for spacing
            sheet.createRow(1);

            // Header row
            Row headerRow = sheet.createRow(2);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Market Name");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Product Barcode");

            // Add data rows
            if (marketId == 0) {
                // Add data for all markets
                addStockRow(sheet, 3, 1, "City Market", 10, "11564");
                addStockRow(sheet, 4, 2, "Main Market", 15, "11565");
                addStockRow(sheet, 5, 3, "Town Market", 20, "11566");
                addStockRow(sheet, 6, 4, "Local Market", 25, "11567");
            } else if (marketId == 1) {
                addStockRow(sheet, 3, 1, "City Market", 10, "11564");
            } else if (marketId == 2) {
                addStockRow(sheet, 3, 2, "Main Market", 15, "11565");
            } else if (marketId == 3) {
                addStockRow(sheet, 3, 3, "Town Market", 20, "11566");
            } else if (marketId == 4) {
                addStockRow(sheet, 3, 4, "Local Market", 25, "11567");
            } else {
                throw new IllegalArgumentException("Invalid market ID");
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to export stock data", e);
        }
    }

    private void addStockRow(Sheet sheet, int rowNum, int id, String marketName, int quantity, String productBarcode) {
        Row dataRow = sheet.createRow(rowNum);
        dataRow.createCell(0).setCellValue(id);
        dataRow.createCell(1).setCellValue(marketName);
        dataRow.createCell(2).setCellValue(quantity);
        dataRow.createCell(3).setCellValue(productBarcode);
    }

    private String getMarketName(Long marketId) {
        switch (marketId.intValue()) {
            case 1: return "City Market";
            case 2: return "Main Market";
            case 3: return "Town Market";
            case 4: return "Local Market";
            default: return "All Markets";
        }
    }
}
