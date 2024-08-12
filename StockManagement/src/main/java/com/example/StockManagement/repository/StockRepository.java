package com.example.StockManagement.repository;

import com.example.StockManagement.data.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Method to find a stock by product ID and market ID
    Optional<Stock> findByProductIdAndMarketId(Long productId, Long marketId);

    // New method to find stocks by market ID
    List<Stock> findByMarketId(Long marketId);
}
