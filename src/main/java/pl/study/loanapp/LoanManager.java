package pl.study.loanapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public class LoanManager {


    @Value("loanapp.base.nightIncomeMultiplier")
    private static int NIGHT_INCOME_MULTIPLIER;
    @Value("loanapp.base.dayIncomeMultiplier")
    private static int DAY_INCOME_MULTIPLIER;

    static LocalTime startOfNight = LocalTime.of(0, 0, 0);
    static LocalTime endOfNight = LocalTime.of(6, 0, 0);

    public LoanManager() {
    }

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

    static boolean isCustomerAllowedForLoan(Customer customer, Loan loan) {

        return isNumberOfTodayLoanApplicationsExceeded(customer.getLoans(), loan.getRequestTime()) &&
                !isLoanAllowed(customer.getMonthlyIncome(), loan);
    }

    static boolean isNumberOfTodayLoanApplicationsExceeded(Set<Loan> loans, LocalDateTime requestTime) {

        long todayLoanRequest = loans.stream().filter(l ->
                Utils.isWithin(l.getRequestTime(), requestTime)).count();
        return todayLoanRequest <= 3;
    }

    static boolean isLoanAllowed(int monthlyIncome, Loan loan) {
        int maxPossibleLoan = monthlyIncome * getIncomeMultiplier(loan.getRequestTime());
        return loan.getAmount() < maxPossibleLoan;
    }

    static private int getIncomeMultiplier(LocalDateTime date) {
        return (date.toLocalTime().isAfter(startOfNight) && date.toLocalTime().isBefore(endOfNight)) ? NIGHT_INCOME_MULTIPLIER : DAY_INCOME_MULTIPLIER;
    }
}
