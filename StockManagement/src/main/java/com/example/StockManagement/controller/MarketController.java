package com.example.StockManagement.controller;

import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.service.MarketService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequestMapping("/api/market")
public class MarketController {

    @Autowired
    private MarketService marketService;

    @GetMapping
    public List<Market> getAllMarkets() {
        return marketService.findAll();
    }
}
