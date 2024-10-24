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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public ByteArrayInputStream exportStockToExcel(Long marketId) {
        List<Stock> stocks = stockRepository.findByMarketId(marketId);
        return createExcel(stocks, "Stock for Market " + marketId);
    }

    public ByteArrayInputStream exportAllStockToExcel() {
        List<Stock> stocks = stockRepository.findAll();
        Map<Long, List<Stock>> stocksByMarket = groupStocksByMarket(stocks);
        return createExcelForAllMarkets(stocksByMarket);
    }

    private ByteArrayInputStream createExcel(List<Stock> stocks, String sheetName) {
        return createExcelWorkbook(new WorkbookOperation() {
            @Override
            public void performOperation(Workbook workbook) {
                Sheet sheet = workbook.createSheet(sheetName);
                writeStockDataToSheet(sheet, stocks);
            }
        });
    }

    private ByteArrayInputStream createExcelForAllMarkets(Map<Long, List<Stock>> stocksByMarket) {
        return createExcelWorkbook(new WorkbookOperation() {
            @Override
            public void performOperation(Workbook workbook) {
                for (Map.Entry<Long, List<Stock>> entry : stocksByMarket.entrySet()) {
                    Long marketId = entry.getKey();
                    List<Stock> marketStocks = entry.getValue();
                    Sheet sheet = workbook.createSheet("Market " + marketId);
                    writeStockDataToSheet(sheet, marketStocks);
                }
            }
        });
    }

    private ByteArrayInputStream createExcelWorkbook(WorkbookOperation operation) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            operation.performOperation(workbook);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

    private void writeStockDataToSheet(Sheet sheet, List<Stock> stocks) {
        createHeaderRow(sheet);
        populateStockRows(sheet, stocks);
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Quantity", "Name", "Market ID", "Product ID", "Barcode"};
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private void populateStockRows(Sheet sheet, List<Stock> stocks) {
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

    private Map<Long, List<Stock>> groupStocksByMarket(List<Stock> stocks) {
        return stocks.stream()
                .collect(Collectors.groupingBy(stock -> stock.getMarket().getId()));
    }

    private interface WorkbookOperation {
        void performOperation(Workbook workbook) throws Exception;
    }
}
