package com.example.StockManagement.controller;

import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.repository.StockRepository;
import com.example.StockManagement.service.MarketService;
import com.example.StockManagement.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/markets")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private MarketService marketService;

    @GetMapping("/stock")
    public String showStockExportPage(Model model) {
        List<Market> markets = marketService.findAll(); // Assuming you have a marketService
        model.addAttribute("markets", markets);
        return "stock"; // This should match the name of your HTML file (stock.html)
    }

    @GetMapping("/{marketId}/stock")
    public ResponseEntity<InputStreamResource> exportStock(@PathVariable Long marketId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String filename = "stock-data-for-market-" + marketId + "_" + timestamp + ".xlsx";
        return generateExportResponse(stockService.exportStockToExcel(marketId), filename);
    }

    @GetMapping("/stock/export-to-excel")
    public ResponseEntity<InputStreamResource> exportAllStock() {
        return generateExportResponse(stockService.exportAllStockToExcel(),
                "all-stock-data.xlsx");
    }

    private ResponseEntity<InputStreamResource> generateExportResponse(ByteArrayInputStream bais, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(bais));
    }
}
