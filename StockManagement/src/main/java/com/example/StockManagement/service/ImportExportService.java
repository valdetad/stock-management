package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ImportExportService {

    public List<Product> parseProductExcel(MultipartFile file) throws IOException {
        List<Product> products = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Product product = new Product();
                product.setId((long) currentRow.getCell(0).getNumericCellValue());
                product.setName(currentRow.getCell(1).getStringCellValue());
                product.setDescription(currentRow.getCell(2).getStringCellValue());
                product.setPrice(currentRow.getCell(3).getNumericCellValue());

                Cell quantityCell = currentRow.getCell(4);
                if (quantityCell != null && quantityCell.getCellType() == CellType.NUMERIC) {
                    product.setQuantity((int) quantityCell.getNumericCellValue());
                } else {
                    product.setQuantity(null);
                }

                products.add(product);
            }
        }

        return products;
    }

    public ByteArrayInputStream exportProductsToExcel(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Description");
            headerRow.createCell(3).setCellValue("Price");
            headerRow.createCell(4).setCellValue("Quantity");

            int rowIndex = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getPrice());


                Integer quantity = product.getQuantity();
                row.createCell(4).setCellValue(quantity != null ? quantity : 0);
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return new ByteArrayInputStream(outputStream.toByteArray());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to export products to Excel file", e);
        }
    }
}
