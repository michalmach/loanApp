package pl.study.loanapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String surname;
    private int monthlyIncome;
    private Set<Loan> loans;

    protected Customer(String name, String surname, int monthlyIncome) {
        this.name = name;
        this.surname = surname;
        this.monthlyIncome = monthlyIncome;
        this.loans = new HashSet<>();
    }

    public boolean hasLoan() {
        return !loans.isEmpty();
    }

    protected void addLoan(Loan loan) {
        loans.add(loan);
    }

    protected int loansCount() {
        return loans.size();
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getMonthlyIncome() {
        return monthlyIncome;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                ", loans=" + loans +
                '}';
    }
}
