package pl.study.loanapp;


import java.time.LocalDateTime;

class Utils {

    public static boolean isWithin(LocalDateTime myDate, LocalDateTime date) {

        return myDate.getDayOfYear() == date.getDayOfYear() &&
                myDate.getDayOfMonth() == date.getDayOfMonth() &&
                myDate.getDayOfWeek() == date.getDayOfWeek();
    }
}
