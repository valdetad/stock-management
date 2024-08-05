package com.example.StockManagement.controller;

import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.service.MarketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

@RequestMapping("/api/market")
public class MarketController {
    @Autowired
    private MarketService marketService;

    @GetMapping
    public List<Market> getAllMarkets() {
        return marketService.findAll();
    }
}

