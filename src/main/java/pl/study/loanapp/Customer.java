package pl.study.loanapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private int monthlyIncome;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Loan> loans;

    Customer() {
    }
    Customer(String name, String surname, int monthlyIncome) {
        this.name = name;
        this.surname = surname;
        this.monthlyIncome = monthlyIncome;
        this.loans = new HashSet<>();
    }

    public boolean hasLoan() {
        return !loans.isEmpty();
    }

    void addLoan(Loan loan) {
        loan.setCustomer(this);
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
