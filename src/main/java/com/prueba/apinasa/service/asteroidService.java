package com.prueba.apinasa.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class asteroidService {

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formatDate = currentDate.format(format);

        return formatDate;
    }

    public static String getStartDate() {
        return asteroidService.getCurrentDate();
    }

    public static String getEndDate(Long days) {
        LocalDate currentDate = LocalDate.parse(asteroidService.getCurrentDate());
        LocalDate endDate = currentDate.plusDays(days);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String endFormatDate = endDate.format(format);

        return endFormatDate;
    }

}
