package pl.study.loanapp;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Main {

    private static Set<Customer> db = new HashSet<>();
    public static void main(String[] args) {


        // Mocked objects should be retrieved from frontend
        Customer customer1 = new Customer("1", "Michal", "Mach", 5000);
        Loan loan1 = new Loan("10", 1000, LocalDateTime.of(2017, 3, 1, 12, 0), LocalDateTime.of(2017, 4, 1, 12, 0), "1.1.1.1");
        Loan loan2 = new Loan("10", 1000, LocalDateTime.of(2017, 3, 1, 12, 0), LocalDateTime.of(2017, 4, 1, 12, 0), "1.1.1.1");
        Loan loan3 = new Loan("10", 1000, LocalDateTime.of(2017, 3, 1, 12, 0), LocalDateTime.of(2017, 4, 1, 12, 0), "1.1.1.1");
        Loan loan4 = new Loan("10", 1000, LocalDateTime.of(2017, 3, 1, 12, 0), LocalDateTime.of(2017, 4, 1, 12, 0), "1.1.1.1");

        System.out.println(customer1.loansCount());
        BusinessLogic.grantLoan(customer1, loan1);
        System.out.println(customer1.loansCount());
        BusinessLogic.grantLoan(customer1, loan2);
        System.out.println(customer1.loansCount());
        BusinessLogic.grantLoan(customer1, loan3);
        System.out.println(customer1.loansCount());
        BusinessLogic.grantLoan(customer1, loan4);
        System.out.println(customer1.loansCount());
        db.add(customer1);

        db.forEach( c -> System.out.println(c.toString()));


    }
}
