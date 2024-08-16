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
        return generateExportResponse(purchaseService.exportPurchasesToPdf(marketId),
                "purchases-for-market-" + marketId + ".pdf");
    }

    private ResponseEntity<InputStreamResource> generateExportResponse(ByteArrayInputStream bais,
                                                                       String filename) {
        if (bais == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + filename);
        return new ResponseEntity<>(new InputStreamResource(bais), headers, HttpStatus.OK);
    }
}
