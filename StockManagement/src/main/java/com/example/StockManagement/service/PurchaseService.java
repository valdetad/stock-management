package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.data.model.Purchase;
import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.data.model.Stock;
import com.example.StockManagement.repository.MarketRepository;
import com.example.StockManagement.repository.StockRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PurchaseService {

    private final MarketRepository marketRepository;
    private final StockRepository stockRepository;

    public PurchaseService(MarketRepository marketRepository, StockRepository stockRepository) {
        this.marketRepository = marketRepository;
        this.stockRepository = stockRepository;
    }

    public ByteArrayInputStream exportPurchasesToPdf(Long marketId) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            String marketName = marketRepository.findById(marketId)
                    .map(Market::getName)
                    .orElse("Unknown Market");
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            addMarketInfo(document, marketName, currentDate);
            addPurchaseTable(document, marketId);

            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (DocumentException | IOException e) {
            return null;
        }
    }

    private void addMarketInfo(Document document, String marketName, String currentDate) throws DocumentException {
        document.add(new Paragraph("Market: " + marketName));
        document.add(new Paragraph("Purchase Date: " + currentDate));
        document.add(new Paragraph(" "));
    }

    private void addPurchaseTable(Document document, Long marketId) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        addTableHeaders(table);

        double overallTotal = marketRepository.findById(marketId)
                .map(market -> addMarketPurchases(table, market))
                .orElse(0.0);

        addOverallTotalRow(table, overallTotal);
        document.add(table);
    }

    private void addTableHeaders(PdfPTable table) {
        table.addCell("Product Name");
        table.addCell("Quantity");
        table.addCell("Price");
        table.addCell("Total");
    }

    private double addMarketPurchases(PdfPTable table, Market market) {
        double overallTotal = 0.0;

        for (Purchase purchase : market.getPurchases()) {
            for (Product product : purchase.getProducts()) {
                int quantity = stockRepository.findByProductIdAndMarketId(product.getId(), market.getId())
                        .map(Stock::getQuantity)
                        .orElse(0);
                double price = product.getPrice();
                double total = quantity * price;

                table.addCell(product.getName());
                table.addCell(String.valueOf(quantity));
                table.addCell(String.format("%.2f", price));
                table.addCell(String.format("%.2f", total));

                overallTotal += total;
            }
        }

        return overallTotal;
    }

    private void addOverallTotalRow(PdfPTable table, double overallTotal) {
        table.addCell("Overall Total");
        table.addCell("");
        table.addCell("");
        table.addCell(String.format("%.2f", overallTotal));
    }
}
