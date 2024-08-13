package com.example.StockManagement.controller;

import com.example.StockManagement.service.StockService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
        try {
            ByteArrayInputStream bais = stockService.exportStockToExcel(marketId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=stock-data-for-market-" + marketId + ".xlsx");

            return new ResponseEntity<>(new InputStreamResource(bais), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stock")
    public ResponseEntity<InputStreamResource> exportAllStock() {
        try {
            ByteArrayInputStream bais = stockService.exportAllStockToExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=all-stock-data.xlsx");

            return new ResponseEntity<>(new InputStreamResource(bais), headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
