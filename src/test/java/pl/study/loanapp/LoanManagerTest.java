package pl.study.loanapp;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class LoanManagerTest {

    Customer customer1;
    Customer customer2;
    Loan loanAtNight;
    Loan loanAtDay;
    private int NIGHT_INCOME_MULTIPLIER = 4;
    private int DAY_INCOME_MULTIPLIER = 6;

    LoanManager loanManager;

    @Before
    public void setUp() throws Exception {

        loanManager = new LoanManager(NIGHT_INCOME_MULTIPLIER, DAY_INCOME_MULTIPLIER);
        customer1 = new Customer("Karol", "Wachowicki", 4000);
        customer2 = new Customer("Mieczyslaw", "Wachowicki", 2000);
        loanAtNight = new Loan(10000, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.of(2017,1,1,1,0));
        loanAtDay = new Loan(10000, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.of(2017,1,1,13,0));
    }

    @Test
    public void grantLoan() throws Exception {

    }

    @Test
    public void isForgePossibleTest() {
        assertFalse(loanManager.isLoanAllowed(customer2.getMonthlyIncome(), loanAtNight));
        assertTrue(loanManager.isLoanAllowed(customer2.getMonthlyIncome(), loanAtDay));
    }

}