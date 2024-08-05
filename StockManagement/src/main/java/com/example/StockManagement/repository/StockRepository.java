package com.example.StockManagement.repository;

import com.example.StockManagement.data.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByMarketId(Long marketId);
}
