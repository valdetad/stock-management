package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Stock;
import com.example.StockManagement.repository.StockRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // Retrieve all stock entries for a specific market
    public List<Stock> findByMarketId(Long marketId) {
        return stockRepository.findByMarketId(marketId);
    }

    // Export stock data to Excel
    public ByteArrayInputStream exportStockToExcel(List<Stock> stocks) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Market Name");
            headerRow.createCell(1).setCellValue("Quantity");
            headerRow.createCell(2).setCellValue("Product Barcode");

            // Populate rows with stock data
            int rowNum = 1;
            for (Stock stock : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getMarket().getName()); // Ensure Market has a getName() method
                row.createCell(1).setCellValue(stock.getQuantity());
                row.createCell(2).setCellValue(stock.getProduct().getBarcode()); // Ensure Product has a getBarcode() method
            }

            // Convert workbook to ByteArrayInputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export stock to Excel", e);
        }
    }
}
