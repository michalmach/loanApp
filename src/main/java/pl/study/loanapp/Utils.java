package pl.study.loanapp;


import java.time.LocalDateTime;

public class Utils {

    public static boolean isWithin(LocalDateTime myDate, LocalDateTime date) {

        return myDate.getDayOfYear() == date.getDayOfYear() &&
                myDate.getDayOfMonth() == myDate.getDayOfMonth() &&
                myDate.getDayOfWeek() == date.getDayOfWeek();
    }
}
