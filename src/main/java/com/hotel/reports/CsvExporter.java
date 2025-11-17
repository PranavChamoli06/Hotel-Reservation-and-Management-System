package com.hotel.reports;

import com.hotel.model.Reservation;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Duration;
import java.util.List;

public class CsvExporter {

    /* Escape + wrap every field to make Excel stable */
    private static String wrap(Object o) {
        if (o == null) return "\"\"";
        String s = o.toString().replace("\"", "\"\"");
        return "\"" + s + "\"";
    }

    public static void export(List<Reservation> reservations, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);

        // Headers
        writer.append("ID,User ID,Guest Name,Room No,Room Type,Check-In,Check-Out,Nights,Phone,Status,Total Price,Created At\n");

        for (Reservation r : reservations) {

            LocalDate in = r.getCheckIn() == null ? null : r.getCheckIn().toLocalDate();
            LocalDate out = r.getCheckOut() == null ? null : r.getCheckOut().toLocalDate();

            long nights = 0;
            if (in != null && out != null) {
                nights = Duration.between(in.atStartOfDay(), out.atStartOfDay()).toDays();
            }

            writer.append(wrap(r.getId())).append(",");
            writer.append(wrap(r.getUserId())).append(",");
            writer.append(wrap(r.getGuestName())).append(",");
            writer.append(wrap(r.getRoomNumber())).append(",");
            writer.append(wrap(r.getRoomType())).append(",");
            writer.append(wrap(in == null ? "" : in.toString())).append(",");
            writer.append(wrap(out == null ? "" : out.toString())).append(",");
            writer.append(wrap(nights)).append(",");
            writer.append(wrap(r.getPhoneNumber())).append(",");
            writer.append(wrap(r.getStatus())).append(",");
            writer.append(wrap(r.getTotalPrice())).append(",");
            writer.append(wrap(r.getCreatedAt())).append("\n");
        }

        writer.flush();
        writer.close();
    }

    public static String dailyFileName(LocalDate date) { return "reservations_day_" + date + ".csv"; }
    public static String monthlyFileName(int y, int m) { return "reservations_month_" + y + "_" + String.format("%02d", m) + ".csv"; }
    public static String yearlyFileName(int y) { return "reservations_year_" + y + ".csv"; }
}
