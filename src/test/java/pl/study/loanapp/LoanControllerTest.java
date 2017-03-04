package pl.study.loanapp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class LoanControllerTest {

    Customer customer1;
    Customer customer2;
    Loan loanAtNight;
    Loan loanAtDay;

    @Autowired
    LoanManager loanController;

    @Before
    public void setUp() throws Exception {

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
        assertFalse(loanController.isLoanAllowed(customer2.getMonthlyIncome(), loanAtNight));
        assertTrue(loanController.isLoanAllowed(customer2.getMonthlyIncome(), loanAtDay));
    }

}