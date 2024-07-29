package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Category;
import com.example.StockManagement.data.model.Product;
import com.univocity.parsers.csv.ExcelParser;
import com.univocity.parsers.csv.ExcelParserSettings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportExportService {


    public List<Product> parseProductExcel(MultipartFile fIle) {
        try {
            List<Product> productList = new ArrayList<>();
            InputStream inputStream = file.getInputStream();

            ExcelParserSettings settings = new ExcelParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setSkipEmptyLines(true);

            ExcelParser parser = new ExcelParser(settings);
            List<String[]> allRows = parser.parseAll(new InputStreamReader(inputStream));

            for (String[] row : allRows) {
                try {
                    if (row.length < 5) {
                        throw new IllegalArgumentException("Excel row does not have enough columns");
                    }
                    Product product = new Product();
                    product.setName(row[1]);

                    try {
                        product.setCategory(Category.valueOf(row[2].toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid category: " + row[2]);
                        System.out.println(e.getMessage());
                        continue;
                    }

                    product.setPrice(Double.parseDouble(row[3]));
                    product.setDescription(row[4]);
                    product.setBarcode(row[5]);
                    productList.add(product);
                } catch (Exception e) {
                    System.err.println("Error processing row: " + e.getMessage());
                }
            }
            System.out.println("HELLO");
            System.out.println(productList.size());
            return productList;
            if (!productList.isEmpty()) {
                productService.saveAll(productList);
            }

            return ResponseEntity.ok("Upload Successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }
}
