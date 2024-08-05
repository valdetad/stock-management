package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImportExportService importExportService;

    @Autowired
    public ProductService(ProductRepository productRepository, ImportExportService importExportService) {
        this.productRepository = productRepository;
        this.importExportService = importExportService;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void saveAll(List<Product> products) {
        if (products != null && !products.isEmpty()) {
            productRepository.saveAll(products);
        }
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> importProducts(MultipartFile file) throws Exception {
        List<Product> products = importExportService.parseProductExcel(file);
        saveAll(products);
        return products;
    }

    public ByteArrayInputStream exportProductsToExcel() {
        List<Product> products = findAll();
        return importExportService.exportProductsToExcel(products);
    }
}
