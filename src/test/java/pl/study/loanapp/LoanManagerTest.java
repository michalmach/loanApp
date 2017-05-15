package pl.study.loanapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.study.loanapp.repository.CustomerRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javafx.scene.input.KeyCode.T;
import static org.junit.Assert.*;

public class LoanManagerTest {

    Customer customer1;
    Customer customer2;
    Loan loanAtNight;
    Loan loanAtDay;
    Loan loanBig;
    Loan loanSmall;

    private static final int NIGHT_INCOME_MULTIPLIER = 4;
    private static final int DAY_INCOME_MULTIPLIER = 6;
    private static final long CUSTOMER_ID = 1L;
    private static final LocalDateTime nighTime = LocalDateTime.of(2017,1,1,1,0);
    private static final LocalDateTime dayTime = LocalDateTime.of(2017,1,5,15,0);

    LoanManager loanManager;

    @Before
    public void setUp() throws Exception {
        loanManager = new LoanManager(NIGHT_INCOME_MULTIPLIER, DAY_INCOME_MULTIPLIER);

        customer1 = new Customer("Karol", "Wachowicki", 4000);
        customer2 = new Customer("Mieczyslaw", "Wachowicki", 2000);
        loanAtNight = new Loan(10000, nighTime, nighTime);
        loanAtDay = new Loan(10000, dayTime, dayTime);
        loanBig = new Loan(3000000, dayTime, dayTime);
        loanSmall = new Loan(100, dayTime, dayTime);
    }

    @Test
    public void getIncomeMultiplierTest() {
        assertEquals(NIGHT_INCOME_MULTIPLIER, loanManager.getIncomeMultiplier(nighTime));
        assertEquals(DAY_INCOME_MULTIPLIER, loanManager.getIncomeMultiplier(dayTime));
    }

    @Test
    public void isLoanAllowedTest() {
        Loan dayLoan = new Loan(20000, nighTime, nighTime);
        dayLoan.setRequestTime(dayTime);

        Loan nightLoan = new Loan(20000, nighTime, nighTime);
        nightLoan.setRequestTime(nighTime);

        //Test loan requested at day time
        assertTrue(loanManager.isLoanAllowed(4000, dayLoan));
        assertFalse(loanManager.isLoanAllowed(1000, dayLoan));

        //Test loan requested at night time
        assertTrue(loanManager.isLoanAllowed(6000, nightLoan));
        assertFalse(loanManager.isLoanAllowed(1000, nightLoan));

    }

    @Test
    public void isNumberOfTodayLoanApplicationsExceededTest() {
        Set<Loan> loans = initLoans();
        assertTrue(loanManager.isNumberOfTodayLoanApplicationsExceeded(loans, dayTime));
        assertFalse(loanManager.isNumberOfTodayLoanApplicationsExceeded(loans, nighTime));
    }

    private Set<Loan> initLoans() {
        Set<Loan> loans = new HashSet<Loan>();
        for (int i = 0; i < 4; i++) {
            Loan loan = new Loan();
            loan.setRequestTime(dayTime);
            loans.add(loan);
        }
        Loan loan = new Loan();
        loan.setRequestTime(nighTime);
        loans.add(loan);

        return loans;
    }

    @Test
    public void isCustomerAllowedForLoanTest() {
        assertTrue(loanManager.isCustomerAllowedForLoan(customer1, loanSmall));
        assertFalse(loanManager.isCustomerAllowedForLoan(customer1, loanBig));
    }

    @Test
    public void isForgePossibleTest() {
        loanAtNight.setRequestTime(nighTime);
        assertFalse(loanManager.isLoanAllowed(customer2.getMonthlyIncome(), loanAtNight));
        loanAtDay.setRequestTime(dayTime);
        assertTrue(loanManager.isLoanAllowed(customer2.getMonthlyIncome(), loanAtDay));
    }

    @Test
    public void applyForLoanTest() throws Exception {
        LoanManager managerSpy = Mockito.spy(loanManager);
        Customer customerSpy = Mockito.spy(customer1);

        managerSpy.customerRepository = Mockito.mock(CustomerRepository.class);
        Mockito.when(managerSpy.customerRepository.findOne(Mockito.anyLong())).thenReturn(customerSpy);
        Mockito.when(managerSpy.customerRepository.save(Mockito.any(Customer.class))).thenReturn(customerSpy);

        //Check status GRANTED
        Loan loanResult = managerSpy.applyForLoan(CUSTOMER_ID, loanSmall);
        assertEquals(Loan.Status.GRANTED, loanResult.getStatus());

        // Check status REJECTED
        loanResult = managerSpy.applyForLoan(CUSTOMER_ID, loanBig);
        assertEquals(Loan.Status.REJECTED, loanResult.getStatus());

        // Check if methods ... are invoked
        Mockito.verify(managerSpy, Mockito.times(2)).saveCustomer(customerSpy);
        Mockito.verify(customerSpy, Mockito.times(1)).addLoan(loanSmall);
        Mockito.verify(customerSpy, Mockito.times(1)).addLoan(loanBig);
        Mockito.verify(customerSpy, Mockito.times(2)).addLoan(Mockito.any(Loan.class));


    }
}
