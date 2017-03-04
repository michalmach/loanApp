package pl.study.loanapp;

import java.util.HashSet;
import java.util.Set;

class Customer {

    private String id;
    private String name;
    private String surname;
    private int monthlyIncome;
    private Set<Loan> loans;

    protected Customer(String id, String name, String surname, int monthlyIncome) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", monthlyIncome=" + monthlyIncome +
                ", loans=" + loans +
                '}';
    }
}
