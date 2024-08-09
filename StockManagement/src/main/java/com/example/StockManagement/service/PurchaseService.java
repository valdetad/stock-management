package com.example.StockManagement.service;

import com.example.StockManagement.data.model.Purchase;
import com.example.StockManagement.repository.PurchaseRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Format the current date
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormatter.format(new Date());

        // Create a title with the current date
        String title = "Purchase Date " + currentDate;
        Paragraph titleParagraph = new Paragraph(title);
        titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(titleParagraph);

        // Add additional content
        Paragraph paragraph = new Paragraph("This is the body of the document.");
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(12);
        paragraph.setFont(fontParagraph);

        document.add(paragraph);

        document.close();
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }
}