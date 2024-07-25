package com.example.StockManagement.repository;

import com.example.StockManagement.data.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
