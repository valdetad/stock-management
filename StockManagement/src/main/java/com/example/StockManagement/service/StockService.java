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

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> getStockByMarketId(Long marketId) {
        return stockRepository.findByMarketId(marketId);
    }

    public ByteArrayInputStream exportStockToExcel(Long marketId) {
        List<Stock> stocks = getStockByMarketId(marketId);
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Stock");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Quantity");
            headerRow.createCell(2).setCellValue("Market ID");
            headerRow.createCell(3).setCellValue("Product ID");

            // Populate data rows
            int rowNum = 1;
            for (Stock stock : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getId());
                row.createCell(1).setCellValue(stock.getQuantity());
                row.createCell(2).setCellValue(stock.getMarket().getId());
                row.createCell(3).setCellValue(stock.getProduct().getId());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public ByteArrayInputStream exportAllStockToExcel() {
        List<Stock> stocks = getAllStock();
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Stock");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Quantity");
            headerRow.createCell(2).setCellValue("Market ID");
            headerRow.createCell(3).setCellValue("Product ID");

            // Populate data rows
            int rowNum = 1;
            for (Stock stock : stocks) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stock.getId());
                row.createCell(1).setCellValue(stock.getQuantity());
                row.createCell(2).setCellValue(stock.getMarket().getId());
                row.createCell(3).setCellValue(stock.getProduct().getId());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}