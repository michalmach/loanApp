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
    public static final int MAX_LOAN_APPLICATIONS_PER_DAY = 3;
    private int NIGHT_INCOME_MULTIPLIER;
    private int DAY_INCOME_MULTIPLIER;

    private LocalTime startOfNight = LocalTime.of(0, 0, 0);
    private LocalTime endOfNight = LocalTime.of(6, 0, 0);

    @Autowired
    protected CustomerRepository customerRepository;

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
        customer.addLoan(loan);
        saveCustomer(customer);
        return loan;
    }


    public Optional<Customer> saveCustomer(Customer customer) {
        Customer dbCustomer = customerRepository.save(customer);
        return Optional.of(dbCustomer);
    }

    // From requirements: extend only for a one week at a time.
    // Interests will be multiplied by a factor of 1.5 each time.
    public Optional<LoanExtension> extendCustomerLoan(long customerId, long loanId, int extensionDaysNum) {
        LoanExtension extension = null;
        Customer customer = customerRepository.getOne(customerId);
        Optional<Loan> loanOptional = customer
                .getLoans()
                .stream()
                .filter(l -> l.getContractId() == loanId).findFirst();

        if (loanOptional.isPresent()) {
            extension = loanOptional.get().extendLoan(extensionDaysNum);
        }
        saveCustomer(customer);

        return Optional.ofNullable(extension);
    }
    public Set<Loan> retrieveHistory(Long customerId) {
        return customerRepository.getOne(customerId).getLoans();
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    boolean isCustomerAllowedForLoan(Customer customer, Loan loan) {

        return !isNumberOfTodayLoanApplicationsExceeded(customer.getLoans(), loan.getRequestTime()) &&
                isLoanAllowed(customer.getMonthlyIncome(), loan);
    }

    boolean isNumberOfTodayLoanApplicationsExceeded(Set<Loan> loans, LocalDateTime requestTime) {

        long todayLoanRequest = loans.stream().filter(l ->
                Utils.isWithin(l.getRequestTime(), requestTime)).count();
        return todayLoanRequest > MAX_LOAN_APPLICATIONS_PER_DAY;
    }

    boolean isLoanAllowed(int monthlyIncome, Loan loan) {
        int maxPossibleLoan = monthlyIncome * getIncomeMultiplier(loan.getRequestTime());
        return loan.getAmount() <= maxPossibleLoan;
    }

    int getIncomeMultiplier(LocalDateTime date) {
        return (date.toLocalTime().isAfter(startOfNight) && date.toLocalTime().isBefore(endOfNight)) ? NIGHT_INCOME_MULTIPLIER : DAY_INCOME_MULTIPLIER;
    }
}
