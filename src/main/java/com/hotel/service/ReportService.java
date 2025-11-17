package com.hotel.service;

import com.hotel.dao.ReservationDAO;
import com.hotel.model.Reservation;
import com.hotel.reports.CsvExporter;
import com.hotel.reports.ExcelExporter;
import com.hotel.reports.PdfExporter;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ReportService {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    /* ========================================================================
       EXPORT: ALL RESERVATIONS
       ======================================================================== */
    public boolean exportCSV(File directory) {
        try {
            List<Reservation> list = reservationDAO.getAllReservations();
            File out = new File(directory, "reservations_all.csv");
            CsvExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* ========================================================================
       EXPORT: DAILY CSV
       ======================================================================== */
    public boolean exportDailyCSV(File directory, LocalDate date) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByDate(date);
            File out = new File(directory, CsvExporter.dailyFileName(date));
            CsvExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* ========================================================================
       EXPORT: MONTHLY CSV
       ======================================================================== */
    public boolean exportMonthlyCSV(File directory, int year, int month) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByMonth(year, month);
            File out = new File(directory, CsvExporter.monthlyFileName(year, month));
            CsvExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* ========================================================================
       EXPORT: YEARLY CSV
       ======================================================================== */
    public boolean exportYearlyCSV(File directory, int year) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByYear(year);
            File out = new File(directory, CsvExporter.yearlyFileName(year));
            CsvExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* =======================================================================
   EXCEL EXPORT
   ======================================================================= */

    public boolean exportExcelAll(File directory) {
        try {
            List<Reservation> list = reservationDAO.getAllReservations();
            File out = new File(directory, ExcelExporter.allFileName());
            ExcelExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportExcelDaily(File directory, LocalDate date) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByDate(date);
            File out = new File(directory, ExcelExporter.dailyFileName(date));
            ExcelExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportExcelMonthly(File directory, int year, int month) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByMonth(year, month);
            File out = new File(directory, ExcelExporter.monthlyFileName(year, month));
            ExcelExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportExcelYearly(File directory, int year) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByYear(year);
            File out = new File(directory, ExcelExporter.yearlyFileName(year));
            ExcelExporter.export(list, out.getAbsolutePath());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* =======================================================================
   PDF EXPORT
   ======================================================================= */

    public boolean exportPDFAll(File directory) {
        try {
            List<Reservation> list = reservationDAO.getAllReservations();
            File out = new File(directory, PdfExporter.allFileName());
            PdfExporter.export(list, out.getAbsolutePath(), "All Reservations Report");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportPDFDaily(File directory, LocalDate date) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByDate(date);
            File out = new File(directory, PdfExporter.dailyFileName(date));
            PdfExporter.export(list, out.getAbsolutePath(), "Daily Report — " + date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportPDFMonthly(File directory, int year, int month) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByMonth(year, month);
            File out = new File(directory, PdfExporter.monthlyFileName(year, month));
            PdfExporter.export(list, out.getAbsolutePath(), "Monthly Report — " + year + "-" + month);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean exportPDFYearly(File directory, int year) {
        try {
            List<Reservation> list = reservationDAO.getReservationsByYear(year);
            File out = new File(directory, PdfExporter.yearlyFileName(year));
            PdfExporter.export(list, out.getAbsolutePath(), "Yearly Report — " + year);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
