
package com.example.StockManagement.repository;

import com.example.StockManagement.data.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductIdAndMarketId(Long productId, Long marketId);
    List<Stock> findByMarketId(Long marketId);
}
