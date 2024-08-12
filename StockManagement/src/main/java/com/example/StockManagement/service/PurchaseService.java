package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Market;
import com.example.StockManagement.data.model.Purchase;
import com.example.StockManagement.repository.MarketRepository;
import com.example.StockManagement.repository.PurchaseRepository;
import com.itextpdf.text.*;
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

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private MarketRepository marketRepository;

    public ByteArrayInputStream exportPurchasesToPdf(Long marketId) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormatter.format(new Date());

        // Fetch market name from database
        Optional<Market> marketOptional = marketRepository.findById(marketId);
        String marketName = marketOptional.map(Market::getName).orElse("Unknown Market");

        // Add market name to the paragraph
        Paragraph paragraph = new Paragraph("Market: " + marketName);

        document.add(paragraph);

        String title = "Purchase Date " + currentDate;
        Paragraph titleParagraph = new Paragraph(title);
        document.add(titleParagraph);

        // Optionally, add more content to the PDF here, such as a list of purchases.

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }
}
