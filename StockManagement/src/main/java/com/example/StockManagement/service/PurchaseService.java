package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.data.model.Purchase;
import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.data.model.Stock;
import com.example.StockManagement.repository.MarketRepository;
import com.example.StockManagement.repository.PurchaseRepository;
import com.example.StockManagement.repository.StockRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final MarketRepository marketRepository;
    private final StockRepository stockRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, MarketRepository marketRepository,
                           StockRepository stockRepository) {
        this.purchaseRepository = purchaseRepository;
        this.marketRepository = marketRepository;
        this.stockRepository = stockRepository;
    }

    public ByteArrayInputStream exportPurchasesToPdf(Long marketId) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = createDocument(out);
            String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            Optional<Market> marketOptional = marketRepository.findById(marketId);
            String marketName = marketOptional.map(Market::getName).orElse("Unknown Market");

            addMarketInfo(document, marketName, currentDate);
            addPurchaseTable(document, marketOptional.orElse(null));

            document.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (DocumentException | IOException e) {
            return null;
        }
    }

    private Document createDocument(ByteArrayOutputStream out) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();
        return document;
    }

    private void addMarketInfo(Document document, String marketName, String currentDate)
            throws DocumentException {
        document.add(new Paragraph("Market: " + marketName));
        document.add(new Paragraph("Purchase Date: " + currentDate));
    }

    private void addPurchaseTable(Document document, Market market)
            throws DocumentException {
        PdfPTable table = createTable();
        double overallTotal = 0.0;

        if (market != null) {
            for (Purchase purchase : market.getPurchases()) {
                overallTotal += addPurchaseRows(table, purchase, market.getId());
            }
        }

        addOverallTotalRow(table, overallTotal);
        document.add(table);
    }

    private PdfPTable createTable() {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Product Name");
        table.addCell("Quantity");
        table.addCell("Price");
        table.addCell("Total");

        return table;
    }

    private double addPurchaseRows(PdfPTable table, Purchase purchase, Long marketId) {
        double overallTotal = 0.0;

        for (Product product : purchase.getProducts()) {
            Optional<Stock> stockOptional = stockRepository.findByProductIdAndMarketId(product.getId(), marketId);
            int quantity = stockOptional.map(Stock::getQuantity).orElse(0);
            double price = product.getPrice();
            double total = quantity * price;

            table.addCell(product.getName());
            table.addCell(String.valueOf(quantity));
            table.addCell(String.format("%.2f", price));
            table.addCell(String.format("%.2f", total));

            overallTotal += total;
        }

        return overallTotal;
    }

    private void addOverallTotalRow(PdfPTable table, double overallTotal) {
        PdfPCell totalCell = new PdfPCell(new Paragraph("Overall Total"));
        totalCell.setColspan(3);
        table.addCell(totalCell);
        table.addCell(String.format("%.2f", overallTotal));
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }
}