package pl.study.loanapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.study.loanapp.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class LoanManager {
    private int NIGHT_INCOME_MULTIPLIER;
    private int DAY_INCOME_MULTIPLIER;

    private LocalTime startOfNight = LocalTime.of(0, 0, 0);
    private LocalTime endOfNight = LocalTime.of(6, 0, 0);

    @Autowired
    private CustomerRepository customerRepository;

    public LoanManager(@Value("${loanapp.base.nightIncomeMultiplier}") int nightIncomeMultiplier,
                       @Value("${loanapp.base.dayIncomeMultiplier}") int dayIncomeMultiplier) {
        this.NIGHT_INCOME_MULTIPLIER = nightIncomeMultiplier;
        this.DAY_INCOME_MULTIPLIER = dayIncomeMultiplier;
    }

    public Loan applyForLoan(Long customerId, Loan loan) {
        Customer customer = customerRepository.findOne(customerId);
        if(isCustomerAllowedForLoan(customer, loan)) {
            loan.setStatus(Loan.Status.GRANTED);
        } else {
            loan.setStatus(Loan.Status.REJECTED);
        }
        loan.setCustomer(customer);
        customerRepository.save(loan);
        customer.addLoan(loan);
        saveCustomer(customer);
        return loan;
    }


    public Optional<Customer> saveCustomer(Customer customer) {
        Customer dbCustomer = customerRepository.save(customer);
        return Optional.of(dbCustomer);
    }

    public void extendCustomersLoan(Loan loan) {

    }
    public Set<Loan> retrieveHistory(Long customerId) {
        return customerRepository.getOne(customerId).getLoans();
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
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
