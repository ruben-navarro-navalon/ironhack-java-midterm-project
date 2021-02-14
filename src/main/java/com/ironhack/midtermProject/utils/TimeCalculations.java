package com.ironhack.midtermProject.utils;

import java.time.LocalDate;
import java.time.Period;

public class TimeCalculations {
    public static Integer calculateYears(LocalDate date) {
        Period age = Period.between(date, LocalDate.now());
        return age.getYears();
    }

    public static Integer calculateMonths(LocalDate date) {
        Period age = Period.between(date, LocalDate.now());
        return age.getMonths();
    }
}
