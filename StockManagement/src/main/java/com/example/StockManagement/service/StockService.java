
package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Stock;
import com.example.StockManagement.repository.StockRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getStockByMarketId(Long marketId) {
        return stockRepository.findByMarketId(marketId);
    }

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    private void writeStockDataToSheet(Sheet sheet, List<Stock> stocks) {
        // header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Quantity");
        headerRow.createCell(2).setCellValue("Name");
        headerRow.createCell(3).setCellValue("Market ID");
        headerRow.createCell(4).setCellValue("Product ID");
        headerRow.createCell(5).setCellValue("Barcode");

        int rowNum = 1;
        for (Stock stock : stocks) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(stock.getId());
            row.createCell(1).setCellValue(stock.getQuantity());
            row.createCell(2).setCellValue(stock.getName());
            row.createCell(3).setCellValue(stock.getMarket().getId());
            row.createCell(4).setCellValue(stock.getProduct().getId());
            row.createCell(5).setCellValue(stock.getBarcode());
        }
    }

    public ByteArrayInputStream exportStockToExcel(Long marketId) {
        List<Stock> stocks = getStockByMarketId(marketId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Stock for Market " + marketId);
            writeStockDataToSheet(sheet, stocks);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    public ByteArrayInputStream exportAllStockToExcel() {
        List<Stock> stocks = getAllStock();
        // Group stocks by market ID
        Map<Long, List<Stock>> stocksByMarket = stocks.stream()
                .collect(Collectors.groupingBy(stock -> stock.getMarket().getId()));

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            for (Map.Entry<Long, List<Stock>> entry : stocksByMarket.entrySet()) {
                Long marketId = entry.getKey();
                List<Stock> marketStocks = entry.getValue();

                // Creating sheet for each market
                Sheet sheet = workbook.createSheet("Market " + marketId);
                writeStockDataToSheet(sheet, marketStocks);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
}
