package com.example.StockManagement.repository;

import com.example.StockManagement.data.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}