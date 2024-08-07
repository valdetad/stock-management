package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {

    @Autowired
    private MarketRepository marketRepository;

    public List<Market> findAll(){
        return marketRepository.findAll();
    }
}