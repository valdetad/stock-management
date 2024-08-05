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

    public List<Stock> findByMarketId(Long marketId) {
        return stockRepository.findByMarketId(marketId);
    }

    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    public ByteArrayInputStream exportStockToExcel(Long marketId) {
        List<Stock> stocks = stockRepository.findByMarketId(marketId);
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Market Name");
            headerRow.createCell(1).setCellValue("Quantity");
            headerRow.createCell(2).setCellValue("Product Barcode");

//            int rowNum = 1;
//            for (Stock stock : stocks) {
//                Row row = sheet.createRow(rowNum++);
//                row.createCell(0).setCellValue(stock.getMarket() != null ? stock.getMarket().getName() : "N/A");
//                row.createCell(1).setCellValue(stock.getQuantity() != null ? stock.getQuantity() : 0);
//                row.createCell(2).setCellValue(stock.getProduct() != null ? stock.getProduct().getBarcode() : "N/A");
//            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export stock to Excel", e);
        }
    }
}
