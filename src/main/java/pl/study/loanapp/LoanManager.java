package pl.study.loanapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Component
public class LoanManager {
    private int NIGHT_INCOME_MULTIPLIER;
    private int DAY_INCOME_MULTIPLIER;

    private LocalTime startOfNight = LocalTime.of(0, 0, 0);
    private LocalTime endOfNight = LocalTime.of(6, 0, 0);

    public LoanManager(@Value("${loanapp.base.nightIncomeMultiplier}") int nightIncomeMultiplier,
                       @Value("${loanapp.base.dayIncomeMultiplier}") int dayIncomeMultiplier) {
        this.NIGHT_INCOME_MULTIPLIER = nightIncomeMultiplier;
        this.DAY_INCOME_MULTIPLIER = dayIncomeMultiplier;
    }

    public void grantLoan(Customer customer, Loan loan) {

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

    public void extendCustomersLoan(Loan loan) {

    }
    public void retrieveHistory(Customer customer) {

    }

    boolean isCustomerAllowedForLoan(Customer customer, Loan loan) {

        return isNumberOfTodayLoanApplicationsExceeded(customer.getLoans(), loan.getRequestTime()) &&
                !isLoanAllowed(customer.getMonthlyIncome(), loan);
    }

    boolean isNumberOfTodayLoanApplicationsExceeded(Set<Loan> loans, LocalDateTime requestTime) {

        long todayLoanRequest = loans.stream().filter(l ->
                Utils.isWithin(l.getRequestTime(), requestTime)).count();
        return todayLoanRequest <= 3;
    }

    boolean isLoanAllowed(int monthlyIncome, Loan loan) {
        int maxPossibleLoan = monthlyIncome * getIncomeMultiplier(loan.getRequestTime());
        return loan.getAmount() < maxPossibleLoan;
    }

    private int getIncomeMultiplier(LocalDateTime date) {
        return (date.toLocalTime().isAfter(startOfNight) && date.toLocalTime().isBefore(endOfNight)) ? NIGHT_INCOME_MULTIPLIER : DAY_INCOME_MULTIPLIER;
    }
}
