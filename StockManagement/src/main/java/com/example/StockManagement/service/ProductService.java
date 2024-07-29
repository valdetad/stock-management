package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImportExportService importExportService;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void saveAll(List<Product> products) {
        if (products != null && !products.isEmpty()) {
            productRepository.saveAll(products);
        }
    }

    public void importProducts(MultipartFile file) {
        List<Product> products = importExportService.parseProductExcel(file);

        saveAll(products);

    }
}
