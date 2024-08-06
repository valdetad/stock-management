package com.example.StockManagement.controller;

import com.example.StockManagement.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/markets")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<byte[]> getStockByMarketId(@PathVariable Long id) {
        try {
            ByteArrayInputStream byteArrayInputStream = stockService.exportStockToExcel(id);
            byte[] excelData = byteArrayInputStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stock_report_" + id + ".xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

            return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{id}/stock/import")
    public ResponseEntity<String> importStock(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            stockService.importStock(id, file);
            return ResponseEntity.ok("Stock imported successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to import stock: " + e.getMessage());
        }
    }
}
