package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Stock;
import com.example.StockManagement.repository.StockRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public ByteArrayInputStream exportStockToExcel(Long marketId) throws Exception {
        List<Stock> stocks = stockRepository.findByMarketId(marketId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Stock ID");
            headerRow.createCell(1).setCellValue("Product Name");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Product Price");
            headerRow.createCell(4).setCellValue("Category");

            // Add data rows
            int rowNum = 1;
            for (Stock stock : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getId());
                row.createCell(1).setCellValue(stock.getProduct().getName());
                row.createCell(2).setCellValue(stock.getQuantity());
                row.createCell(3).setCellValue(stock.getProduct().getPrice());
                row.createCell(4).setCellValue(stock.getProduct().getCategory().name());
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return new ByteArrayInputStream(outputStream.toByteArray());
            }
        }
    }
}