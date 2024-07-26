package com.example.StockManagement.controller;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.data.model.Category; // Import the Category enum
import com.example.StockManagement.service.ProductService;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            List<Product> productList = new ArrayList<>();
            InputStream inputStream = file.getInputStream();

            CsvParserSettings settings = new CsvParserSettings();
            settings.setHeaderExtractionEnabled(true);
            settings.setSkipEmptyLines(true);

            CsvParser parser = new CsvParser(settings);
            List<String[]> allRows = parser.parseAll(new InputStreamReader(inputStream));

            for (String[] row : allRows) {
                try {
                    Product product = new Product();
                    product.setName(row[0]);
                    product.setCategory(Category.valueOf(row[1].toUpperCase()));
                    product.setPrice(Double.parseDouble(row[2]));
                    product.setDescription(row[3]);
                    product.setBarcode(row[4]);

                    productList.add(product);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            productService.saveAll(productList);

            return "Upload Successful";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }
    }
}
