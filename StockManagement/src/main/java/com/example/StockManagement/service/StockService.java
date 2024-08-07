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
            if (marketId == 0) {
                createMarketSheet(workbook, 1L, "City Market");
                createMarketSheet(workbook, 2L, "Main Market");
                createMarketSheet(workbook, 3L, "Town Market");
                createMarketSheet(workbook, 4L, "Local Market");
            } else {
                // Single sheet for each market
                createMarketSheet(workbook, marketId, getMarketName(marketId));
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to export stock data", e);
        }
    }

    private void createMarketSheet(Workbook workbook, Long marketId, String marketName) {
        Sheet sheet = workbook.createSheet(marketName);

        Row marketNameRow = sheet.createRow(0);
        marketNameRow.createCell(0).setCellValue("Market Name: " + marketName);

        // Header
        Row headerRow = sheet.createRow(2);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Product Name");
        headerRow.createCell(2).setCellValue("Quantity");
        headerRow.createCell(3).setCellValue("Product Barcode");

        // Data rows
        int rowNum = 3;
        if (marketId == 1) {
            addStockRow(sheet, rowNum++, 1, "Smartphone", 10, "11564");
        } else if (marketId == 2) {
            addStockRow(sheet, rowNum++, 2, "Orange", 15, "11565");
        } else if (marketId == 3) {
            addStockRow(sheet, rowNum++, 3, "Tomato", 20, "11566");
        } else if (marketId == 4) {
            addStockRow(sheet, rowNum++, 4, "Apple", 25, "11567");
        } else if (marketId == 0) {
            addStockRow(sheet, rowNum++, 1, "Smartphone", 10, "11564");
            addStockRow(sheet, rowNum++, 2, "Orange", 15, "11565");
            addStockRow(sheet, rowNum++, 3, "Tomato", 20, "11566");
            addStockRow(sheet, rowNum++, 4, "Apple", 25, "11567");
        } else {
            throw new IllegalArgumentException("Invalid market ID");
        }
    }

    private void addStockRow(Sheet sheet, int rowNum, int id, String productName, int quantity, String productBarcode) {
        Row dataRow = sheet.createRow(rowNum);
        dataRow.createCell(0).setCellValue(id);
        dataRow.createCell(1).setCellValue(productName);
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