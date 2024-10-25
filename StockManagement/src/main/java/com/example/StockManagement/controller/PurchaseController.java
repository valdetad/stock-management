package com.example.StockManagement.controller;

import com.example.StockManagement.repository.MarketRepository;
import com.example.StockManagement.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@Controller
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private MarketRepository marketRepository; // Add this to fetch markets

    // Method to serve the Thymeleaf view
    @GetMapping("/export")
    public String showExportPurchasesPage(Model model) {
        model.addAttribute("markets", marketRepository.findAll());
        return "purchases"; // This should match your Thymeleaf template name (exportPurchases.html)
    }

    @ResponseBody
    @GetMapping("/{marketId}/export")
    public ResponseEntity<InputStreamResource> exportPurchases(@PathVariable Long marketId) {
        ByteArrayInputStream bais = purchaseService.exportPurchasesToPdf(marketId);
        if (bais == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=purchase-for-market-" + marketId + ".pdf");
        return new ResponseEntity<>(new InputStreamResource(bais), headers, HttpStatus.OK);
    }
}
