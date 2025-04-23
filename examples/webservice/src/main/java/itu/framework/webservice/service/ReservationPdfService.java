package itu.framework.webservice.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import itu.framework.webservice.dto.PassengerDto;
import itu.framework.webservice.dto.ReservationDto;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.swing.plaf.ColorUIResource;

@Service
public class ReservationPdfService {

    public byte[] buildPdf(ReservationDto r) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, out);
            doc.open();

            // Titre
            Font title = new Font(Font.HELVETICA, 16, Font.BOLD);
            doc.add(new Paragraph("Reservation #" + r.id + " — " + r.status, title));
            doc.add(new Paragraph(" "));
            
            // Infos vol
            Font h = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font p = new Font(Font.HELVETICA, 11, Font.NORMAL);
            doc.add(new Paragraph("Flight", h));
            doc.add(new Paragraph(
                String.format("%s · %s → %s · %s",
                    nullSafe(r.flightNumber),
                    nullSafe(r.departureCity),
                    nullSafe(r.arrivalCity),
                    nullSafe(r.airplane)
                ), p));
            doc.add(new Paragraph(
                String.format("Departure: %s | Arrival: %s",
                    nullSafe(r.departureTime),
                    nullSafe(r.arrivalTime)
                ), p));
            if (r.createdAt != null) {
                doc.add(new Paragraph("Created at: " + r.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), p));
            }
            doc.add(new Paragraph(" "));

            // Montants
            doc.add(new Paragraph("Amounts", h));
            doc.add(new Paragraph("Total amount: Ar " + nf(r.totalAmount), p));
            doc.add(new Paragraph("Total discount: Ar " + nf(r.totalDiscount), p));
            doc.add(new Paragraph(" "));

            // Tableau passagers
            doc.add(new Paragraph("Passengers", h));
            PdfPTable table = new PdfPTable(new float[]{3f, 2f, 2f, 2f, 2f});
            table.setWidthPercentage(100);
            addHeader(table, "Name", "Birthdate", "Type", "Class", "Final Price");
            if (r.passengers != null) {
                for (PassengerDto psg : r.passengers) {
                    table.addCell(cell(psg.passengerName));
                    table.addCell(cell(psg.passengerBirthdate));
                    table.addCell(cell(psg.passengerType));
                    table.addCell(cell(psg.className));
                    table.addCell(cell("Ar " + nf(psg.finalPrice)));
                }
            }
            doc.add(table);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed: " + e.getMessage(), e);
        }
    }

    private static void addHeader(PdfPTable t, String... labels) {
        Font f = new Font(Font.HELVETICA, 11, Font.BOLD);
        for (String lab : labels) {
            PdfPCell c = new PdfPCell(new Phrase(lab, f));
            c.setBackgroundColor(new ColorUIResource(240, 240, 240));
            c.setPadding(6);
            t.addCell(c);
        }
    }

    private static PdfPCell cell(String txt) {
        PdfPCell c = new PdfPCell(new Phrase(txt != null ? txt : ""));
        c.setPadding(6);
        return c;
    }

    private static String nf(Double v) {
        return v == null ? "-" : String.format("%,.0f", v).replace(',', ' ');
    }

    private static String nullSafe(Object v) {
        return v == null ? "-" : Objects.toString(v);
    }
}
