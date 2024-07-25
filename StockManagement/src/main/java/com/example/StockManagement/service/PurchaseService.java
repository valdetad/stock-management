package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.data.model.Purchase;
import com.example.StockManagement.repository.ProductRepository;
import com.example.StockManagement.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private PurchaseRepository purchaseRepository;

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

}

