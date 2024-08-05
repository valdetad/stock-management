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
import java.util.logging.Logger;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final Logger logger = Logger.getLogger(StockService.class.getName());

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> findByMarketId(Long marketId) {
        List<Stock> stocks = stockRepository.findByMarketId(marketId);
        logger.info("Fetched stocks: " + stocks);  // Log fetched stocks
        return stocks;
    }

    public ByteArrayInputStream exportStockToExcel(List<Stock> stocks) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Stock");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Stock ID");
            headerRow.createCell(1).setCellValue("Market ID");
            headerRow.createCell(2).setCellValue("Product ID");
            headerRow.createCell(3).setCellValue("Quantity");

            int rowNum = 1;
            for (Stock stock : stocks) {
                logger.info("Writing stock to Excel: " + stock);  // Log each stock
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getId());
                row.createCell(1).setCellValue(stock.getMarket().getId());
                row.createCell(2).setCellValue(stock.getProduct().getId());
                row.createCell(3).setCellValue(stock.getQuantity());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to export stock to Excel", e);
        }
    }
}
