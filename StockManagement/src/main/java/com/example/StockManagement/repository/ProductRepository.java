package com.example.StockManagement.repository;

import com.example.StockManagement.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
