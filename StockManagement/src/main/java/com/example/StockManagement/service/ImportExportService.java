package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Category;
import com.example.StockManagement.data.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportExportService {

    public List<Product> parseProductExcel(MultipartFile file) throws Exception {
        List<Product> products = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip header row
                    continue;
                }

                Product product = new Product();
                product.setId((long) row.getCell(0).getNumericCellValue());
                product.setName(row.getCell(1).getStringCellValue());
                product.setCategory(Category.valueOf(row.getCell(2).getStringCellValue()));
                product.setPrice(row.getCell(3).getNumericCellValue());
                product.setDescription(row.getCell(4).getStringCellValue());
                product.setBarcode(row.getCell(5).getStringCellValue());
                product.setQuantity((int) row.getCell(6).getNumericCellValue());

                products.add(product);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file", e);
        }

        return products;
    }

    public ByteArrayInputStream exportProductsToExcel(List<Product> products) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Product ID");
            headerRow.createCell(1).setCellValue("Product Name");
            headerRow.createCell(2).setCellValue("Category");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Description");
            headerRow.createCell(5).setCellValue("Barcode");
            headerRow.createCell(6).setCellValue("Quantity");

            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(String.valueOf(product.getCategory()));
                row.createCell(3).setCellValue(product.getPrice());
                row.createCell(4).setCellValue(product.getDescription());
                row.createCell(5).setCellValue(product.getBarcode());
                row.createCell(6).setCellValue(product.getQuantity());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }
}
