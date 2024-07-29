package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Category;
import com.example.StockManagement.data.model.Product;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportExportService {

    public List<Product> parseProductExcel(MultipartFile file) {
        List<Product> productList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream()) {
            CsvParserSettings settings = createCsvParserSettings();
            CsvParser parser = new CsvParser(settings);
            List<String[]> allRows = parser.parseAll(new InputStreamReader(inputStream));

            for (int i = 0; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                try {
                    Product product = createProductFromRow(row, i + 1);
                    productList.add(product);
                } catch (Exception e) {
                    System.err.println("Error processing row " + (i + 1) + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }

        return productList;
    }

    private CsvParserSettings createCsvParserSettings() {
        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaderExtractionEnabled(true);
        settings.setSkipEmptyLines(true);
        return settings;
    }

    private Product createProductFromRow(String[] row, int rowNum) {
        if (row.length < 5) {
            throw new IllegalArgumentException("CSV row does not have enough columns");
        }

        Product product = new Product();
        product.setName(row[0]);

        try {
            product.setCategory(Category.valueOf(row[1].toUpperCase()));
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category at row " + rowNum + ": " + row[1]);
            throw e;
        }

        try {
            product.setPrice(Double.parseDouble(row[2]));
        } catch (NumberFormatException e) {
            System.err.println("Invalid price at row " + rowNum + ": " + row[2]);
            throw e;
        }

        product.setDescription(row[3]);
        product.setBarcode(row[4]);

        return product;
    }
}
