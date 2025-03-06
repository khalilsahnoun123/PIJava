package com.example.demo.utils;

import com.example.demo.models.models_TP.Reservation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExcelExporter {

    public static void exportReservationsToExcel(List<Reservation> reservations, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reservations");

        // Create header row with additional columns
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code Reservation  ",
                "Reservation Date",
                "Travel Date",
                "Seats",
                "Category",
                "Status",
                "Total Price",
                "Vehicule Code",
                "Vehicule Type",
                "Ligne Name",
                "Departure",
                "Arrival"
        };
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate rows with reservation data
        int rowIndex = 1;
        for (Reservation res : reservations) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;
            // ID
            row.createCell(colIndex++).setCellValue(res.getReservationId());
            // Reservation Date
            row.createCell(colIndex++).setCellValue(res.getReservationDate().toString());
            // Travel Date
            row.createCell(colIndex++).setCellValue(res.getTravelDate().toString());
            // Seats
            row.createCell(colIndex++).setCellValue(res.getNumberOfSeats());
            // Ticket Category
            row.createCell(colIndex++).setCellValue(res.getTicketCategory().toString());
            // Status
            row.createCell(colIndex++).setCellValue(res.getStatus().toString());
            // Total Price
            row.createCell(colIndex++).setCellValue(res.getTotalPrice());
            // Vehicule ID
            row.createCell(colIndex++).setCellValue(
                    res.getVehicule() != null ? res.getVehicule().getId() : 0
            );
            // Vehicule Type
            row.createCell(colIndex++).setCellValue(
                    res.getVehicule() != null ? res.getVehicule().getType().toString() : "N/A"
            );
            // Ligne Name (from vehicule's ligne if available)
            String ligneName = "N/A";
            if (res.getVehicule() != null && res.getVehicule().getLigne() != null) {
                ligneName = res.getVehicule().getLigne().getName();
            }
            row.createCell(colIndex++).setCellValue(ligneName);
            // Departure (using departStation name)
            row.createCell(colIndex++).setCellValue(
                    res.getDepartStation() != null ? res.getDepartStation().getNom() : "N/A"
            );
            // Arrival (using finStation name)
            row.createCell(colIndex++).setCellValue(
                    res.getFinStation() != null ? res.getFinStation().getNom() : "N/A"
            );
        }

        // Auto-size all columns for better readability
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // *** Summary Calculations ***
        // Example 1: Total Price for Confirmed Reservations
        double confirmedTotal = reservations.stream()
                .filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus().toString()))
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        // Example 2: Total Price for Canceled Reservations
        double canceledTotal = reservations.stream()
                .filter(r -> "CANCELLED".equalsIgnoreCase(r.getStatus().toString()))
                .mapToDouble(Reservation::getTotalPrice)
                .sum();

        // Example 3: Total Price per Ticket Category
        Map<String, Double> totalPerCategory = reservations.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getTicketCategory().toString(),
                        Collectors.summingDouble(Reservation::getTotalPrice)
                ));

        int summaryStartRow = rowIndex + 2; // leave one blank row

        // Write summary for statuses
        Row statusSummaryRow = sheet.createRow(summaryStartRow);
        statusSummaryRow.createCell(0).setCellValue("Total Confirmed Price:");
        statusSummaryRow.createCell(1).setCellValue(confirmedTotal);

        Row statusSummaryRow2 = sheet.createRow(summaryStartRow + 1);
        statusSummaryRow2.createCell(0).setCellValue("Total Canceled Price:");
        statusSummaryRow2.createCell(1).setCellValue(canceledTotal);

        // Write summary per category
        int catSummaryRow = summaryStartRow + 3;
        for (Map.Entry<String, Double> entry : totalPerCategory.entrySet()) {
            Row row = sheet.createRow(catSummaryRow++);
            row.createCell(0).setCellValue("Total Price for " + entry.getKey() + ":");
            row.createCell(1).setCellValue(entry.getValue());
        }

        // Write the output to file
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
        workbook.close();
    }
}
