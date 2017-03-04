package pl.study.loanapp;

import java.time.LocalTime;

public class BusinessLogic {


    private static int incomeMultiplier = 4;

    public static void grantLoan(Customer customer, Loan loan) {

        loan.setStatus(Loan.Status.PENDING);
        if(isCustomerAllowedForLoan(customer, loan)) {
            loan.setStatus(Loan.Status.GRANTED);
            customer.addLoan(loan);
            //TODO:saveInDb();
            //TODO:notifyFrontend(Decision and others);
        } else {
            loan.setStatus(Loan.Status.REJECTED);
            //TODO:notifyFrontend(Decision and others);
        }

    }

    public static void extendCustomersLoan(Loan loan) {

    }
    public static void retrieveHistory(Customer customer) {

    }

    private static boolean isCustomerAllowedForLoan(Customer customer, Loan loan) {

        return checkNumberOfTodayLoanApplications(customer, loan) &&
                !checkPossibleForgeryBasedOnTime(loan) &&
                checkLoanAmountComparedToIncome(customer, loan);
    }

    private static boolean checkNumberOfTodayLoanApplications(Customer customer, Loan loan) {

        long todayLoanRequest = customer.getLoans().stream().filter(l ->
                Utils.isWithin(l.getRequestTime(), loan.getRequestTime())).count();
        return todayLoanRequest <= 3;
    }

    private static boolean checkPossibleForgeryBasedOnTime(Loan loan) {
        return loan.getRequestTime().toLocalTime().isAfter(LocalTime.of(0, 0, 0)) && loan.getRequestTime().toLocalTime().isBefore(LocalTime.of(6, 0, 0));
    }



    private static boolean checkLoanAmountComparedToIncome (Customer customer, Loan loan) {
        return customer.getMonthlyIncome()*incomeMultiplier >= loan.getAmount();

    }

}
