package com.example.StockManagement.controller;

import com.example.StockManagement.service.StockService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/markets")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{marketId}/stock")
    public ResponseEntity<InputStreamResource> exportStock(@PathVariable Long marketId) {
        return generateExportResponse(stockService.exportStockToExcel(marketId),
                "stock-data-for-market-" + marketId + ".xlsx");
    }

    @GetMapping("/stock")
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
