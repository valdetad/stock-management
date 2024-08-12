package com.example.StockManagement.controller;

import com.example.StockManagement.service.PurchaseService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

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
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/export-all")
    public ResponseEntity<InputStreamResource> exportAllPurchases() {
        try {
            // Implement method to export all purchases
            ByteArrayInputStream bais = purchaseService.exportPurchasesToPdf(null);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=all-purchases.pdf");

            return new ResponseEntity<>(new InputStreamResource(bais), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
