package com.example.StockManagement.controller;

import com.example.StockManagement.data.model.Stock;
import com.example.StockManagement.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/markets")
public class StockController {

    private final StockService stockService;
    private final Logger logger = Logger.getLogger(StockController.class.getName());

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<byte[]> exportStock(@PathVariable Long id) {
        List<Stock> stocks = stockService.findByMarketId(id);
        logger.info("Fetched stocks for market ID " + id + ": " + stocks); // Log fetched stocks
        if (stocks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        ByteArrayInputStream excelData = stockService.exportStockToExcel(stocks);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock_report.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        try {
            return new ResponseEntity<>(excelData.readAllBytes(), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error exporting stock to Excel: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
