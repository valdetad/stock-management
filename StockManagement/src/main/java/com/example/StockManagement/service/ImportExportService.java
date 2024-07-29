package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Category;
import com.example.StockManagement.data.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

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

            for (String[] row : allRows) {
                try {
                    Product product = createProductFromRow(row);
                    productList.add(product);
                } catch (Exception e) {
                    System.err.println("Error processing row: " + e.getMessage());
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

    private Product createProductFromRow(String[] row) {
        if (row.length < 6) {
            throw new IllegalArgumentException("Excel row does not have enough columns");
        }

        Product product = new Product();
        product.setName(row[0]);

        try {
            product.setCategory(Category.valueOf(row[1].toUpperCase()));
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category: " + row[1]);
            throw e;
        }

        product.setPrice(Double.parseDouble(row[2]));
        product.setDescription(row[3]);
        product.setBarcode(row[4]);

        return product;
    }
}
