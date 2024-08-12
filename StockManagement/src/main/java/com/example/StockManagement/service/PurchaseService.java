package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Product;
import com.example.StockManagement.data.model.Purchase;
import com.example.StockManagement.repository.PurchaseRepository;
import com.example.StockManagement.repository.StockRepository;
import com.example.StockManagement.data.model.Stock;
import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.repository.MarketRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private StockRepository stockRepository;

    public ByteArrayInputStream exportPurchasesToPdf(Long marketId) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormatter.format(new Date());

        Optional<Market> marketOptional = marketRepository.findById(marketId);
        String marketName = marketOptional.map(Market::getName).orElse("Unknown Market");

        Paragraph paragraph = new Paragraph("Market: " + marketName);
        document.add(paragraph);

        String title = "Purchase Date: " + currentDate;
        Paragraph titleParagraph = new Paragraph(title);
        document.add(titleParagraph);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        table.addCell("Product Name");
        table.addCell("Quantity");
        table.addCell("Price");
        table.addCell("Total");

        Set<Purchase> purchases = marketOptional.map(Market::getPurchases).orElse(Set.of());
        double overallTotal = 0.0;

        for (Purchase purchase : purchases) {
            for (Product product : purchase.getProducts()) {
                Optional<Stock> stock = stockRepository.findByProductIdAndMarketId(product.getId(), marketId);
                int quantity = stock.isPresent() ? stock.get().getQuantity() : 0;
                double price = product.getPrice();
                double total = quantity * price;
                overallTotal += total;

                table.addCell(product.getName());
                table.addCell(String.valueOf(quantity));
                table.addCell(String.format("%.2f", price));
                table.addCell(String.format("%.2f", total));
            }
        }

        PdfPCell totalCell = new PdfPCell(new Paragraph("Overall Total"));
        totalCell.setColspan(3);
        table.addCell(totalCell);
        table.addCell(String.format("%.2f", overallTotal));

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }
}