package com.example.StockManagement.controller;

import com.example.StockManagement.service.PurchaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/{marketId}/export")
    public ResponseEntity<InputStreamResource> exportPurchases(@PathVariable Long marketId) {
        try {
            ByteArrayInputStream bais = purchaseService.exportPurchasesToPdf(marketId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=purchases-for-market-" + marketId + ".pdf");

            return new ResponseEntity<>(new InputStreamResource(bais), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error exporting purchases for marketId {}: {}", marketId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export-all")
    public ResponseEntity<InputStreamResource> exportAllPurchases() {
        try {
            ByteArrayInputStream bais = purchaseService.exportPurchasesToPdf(null); // Adjust as needed
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=all-purchases.pdf");

            return new ResponseEntity<>(new InputStreamResource(bais), headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error exporting all purchases: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}