package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Category;
import com.example.StockManagement.data.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ImportExportService {

    public List<Product> parseProductExcel(MultipartFile file) {
        List<Product> productList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             //Apache POI class for handling Excel files
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    productList.add(createProductFromRow(row));
                } catch (Exception e) {
                    System.err.println("Error processing row " + row.getRowNum() + ": " + e.getMessage());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage(), e);
        }

        return productList;
    }

    private Product createProductFromRow(Row row) {
        Product product = new Product();

        product.setName(getCellValue(row.getCell(1)));
        product.setCategory(parseCategory(getCellValue(row.getCell(2))));
        product.setPrice(parseDouble(getCellValue(row.getCell(3))));
        product.setDescription(getCellValue(row.getCell(4)));
        product.setBarcode(getCellValue(row.getCell(5)));

        return product;
    }

    private String getCellValue(Cell cell) {
        return Optional.ofNullable(cell)
                .map(this::extractCellValue)
                .orElse("");
    }

    private String extractCellValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> Double.toString(cell.getNumericCellValue());
            case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private Category parseCategory(String categoryString) {
        try {
            return Category.valueOf(categoryString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category: " + categoryString);
            throw e;
        }
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid price: " + value);
            throw e;
        }
    }
}
