package de.htwberlin.webtech.expensetracker.utils;

import java.time.LocalDate;

public   class DateUtils {


    public static boolean isDateInRange(LocalDate start, LocalDate finish, LocalDate dateToCheck){
        if(finish == null && !dateToCheck.isBefore(start)) return true;
        else if( !dateToCheck.isBefore(start) && !dateToCheck.isAfter(finish)) return true;
        else return false;
    }
}
