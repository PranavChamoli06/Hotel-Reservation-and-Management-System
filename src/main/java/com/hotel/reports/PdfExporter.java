package com.hotel.reports;

import com.hotel.model.Reservation;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Font;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;

import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import java.awt.Color;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;


public class PdfExporter {

    public static void export(List<Reservation> reservations, String filePath, String titleText) throws Exception {

        Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // ===== TITLE =====
        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
        Paragraph title = new Paragraph(titleText, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // ===== TABLE =====
        PdfPTable table = new PdfPTable(12); // 12 columns
        table.setWidthPercentage(100);

        String[] headers = {
                "ID", "User ID", "Guest", "Room No", "Room Type",
                "Check-In", "Check-Out", "Nights", "Phone",
                "Status", "Total Price", "Created At"
        };

        Font headFont = new Font(Font.HELVETICA, 12, Font.BOLD);

        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(Color.LIGHT_GRAY);
            table.addCell(cell);
        }

        // ===== ROWS =====
        for (Reservation r : reservations) {

            String in = r.getCheckIn() == null ? "" : r.getCheckIn().toLocalDate().toString();
            String out = r.getCheckOut() == null ? "" : r.getCheckOut().toLocalDate().toString();

            long nights = 0;
            if (r.getCheckIn() != null && r.getCheckOut() != null) {
                nights = Duration.between(
                        r.getCheckIn().toLocalDate().atStartOfDay(),
                        r.getCheckOut().toLocalDate().atStartOfDay()
                ).toDays();
            }

            table.addCell(String.valueOf(r.getId()));
            table.addCell(String.valueOf(r.getUserId()));
            table.addCell(r.getGuestName());
            table.addCell(String.valueOf(r.getRoomNumber()));
            table.addCell(r.getRoomType());
            table.addCell(in);
            table.addCell(out);
            table.addCell(String.valueOf(nights));
            table.addCell(r.getPhoneNumber());
            table.addCell(r.getStatus());
            table.addCell(String.valueOf(r.getTotalPrice()));
            table.addCell(r.getCreatedAt() == null ? "" : r.getCreatedAt().toString());
        }

        document.add(table);

        document.close();
    }

    public static String dailyFileName(LocalDate date) {
        return "reservations_day_" + date + ".pdf";
    }

    public static String monthlyFileName(int year, int month) {
        return "reservations_month_" + year + "_" + String.format("%02d", month) + ".pdf";
    }

    public static String yearlyFileName(int year) {
        return "reservations_year_" + year + ".pdf";
    }

    public static String allFileName() {
        return "reservations_all.pdf";
    }
}
