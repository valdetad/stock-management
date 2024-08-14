package com.example.StockManagement.repository;

import com.example.StockManagement.data.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketRepository extends JpaRepository<Market, Long> {
}