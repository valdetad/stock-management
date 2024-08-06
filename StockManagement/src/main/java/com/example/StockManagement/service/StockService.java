package com.example.StockManagement.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

@Service
public class StockService {

    // This method imports stock data from an Excel file
    public void importStock(Long marketId, MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Process each row and cell as needed
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            String stringValue = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            double numericValue = cell.getNumericCellValue();
                            break;
                        case BOOLEAN:
                            boolean booleanValue = cell.getBooleanCellValue();
                            break;
                        case FORMULA:
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public ByteArrayInputStream exportStockToExcel(Long marketId) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock Data");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Market Name");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Product Barcode");


            // Example data - replace with actual data retrieval
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("1");
            dataRow.createCell(1).setCellValue("City Market");
            dataRow.createCell(2).setCellValue("10");
            dataRow.createCell(3).setCellValue("11564");

            // Write the workbook to an output stream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            throw new RuntimeException("Failed to export stock data", e);
        }
    }
}
