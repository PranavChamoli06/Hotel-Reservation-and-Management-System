package com.hotel.reports;

import com.hotel.model.Reservation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class ExcelExporter {

    public static void export(List<Reservation> reservations, String filePath) throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reservations");

        // ===== HEADER STYLE =====
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        // ===== CREATE HEADER =====
        Row header = sheet.createRow(0);

        String[] columns = {
                "ID", "User ID", "Guest Name", "Room No", "Room Type",
                "Check-In", "Check-Out", "Nights", "Phone",
                "Status", "Total Price", "Created At"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // ===== FILL ROWS =====
        int rowIndex = 1;

        for (Reservation r : reservations) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(r.getId());
            row.createCell(1).setCellValue(r.getUserId());
            row.createCell(2).setCellValue(nullSafe(r.getGuestName()));
            row.createCell(3).setCellValue(r.getRoomNumber());
            row.createCell(4).setCellValue(nullSafe(r.getRoomType()));

            // Convert java.sql.Date → LocalDate safely
            Date cin = r.getCheckIn();
            Date cout = r.getCheckOut();

            row.createCell(5).setCellValue(cin == null ? "" : cin.toLocalDate().toString());
            row.createCell(6).setCellValue(cout == null ? "" : cout.toLocalDate().toString());

            // Nights calculation
            long nights = 0;
            if (cin != null && cout != null) {
                nights = Duration.between(
                        cin.toLocalDate().atStartOfDay(),
                        cout.toLocalDate().atStartOfDay()
                ).toDays();
            }
            row.createCell(7).setCellValue(nights);

            row.createCell(8).setCellValue(nullSafe(r.getPhoneNumber()));
            row.createCell(9).setCellValue(nullSafe(r.getStatus()));
            row.createCell(10).setCellValue(r.getTotalPrice());

            Timestamp created = r.getCreatedAt();
            row.createCell(11).setCellValue(created == null ? "" : created.toString());
        }

        // ===== AUTO-SIZE COLUMNS =====
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // ===== SAVE FILE =====
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    private static String nullSafe(Object o) {
        return o == null ? "" : o.toString();
    }

    public static String dailyFileName(LocalDate date) {
        return "reservations_day_" + date + ".xlsx";
    }

    public static String monthlyFileName(int year, int month) {
        return "reservations_month_" + year + "_" + String.format("%02d", month) + ".xlsx";
    }

    public static String yearlyFileName(int year) {
        return "reservations_year_" + year + ".xlsx";
    }

    public static String allFileName() {
        return "reservations_all.xlsx";
    }
}
