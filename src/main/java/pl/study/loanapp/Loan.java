package pl.study.loanapp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Loan {

    public static final int MAX_INTEREST_PERCENTAGE_PER_DAY = 10;
    public static final double INTEREST_PER_DAY_EXTENSION_FACTOR = 1.5;
    public static final double INETERSTS_PER_DAY_BASE = 1.0;
    public static final int NUM_DAYS_OF_WEEK = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer amount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private LocalDateTime requestTime;
    private double interestPercentagePerDay;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LoanExtension> extensions;

    Loan() {
        this.status = Status.NEW;
        this.requestTime = LocalDateTime.now();
        this.extensions = new ArrayList<>();
        this.interestPercentagePerDay = INETERSTS_PER_DAY_BASE;
    }

    public Loan(int amount, LocalDateTime fromDate, LocalDateTime toDate) {
        this();
        this.amount = amount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public LoanExtension extendLoan(int extensionDaysNum) {

        double interestFactor = computeInterests(extensionDaysNum);

        LoanExtension extension = new LoanExtension(extensionDaysNum);
        extension.setLoan(this);
        if (interestFactor <= MAX_INTEREST_PERCENTAGE_PER_DAY) {
            this.interestPercentagePerDay = interestFactor;
            toDate.plusDays(extensionDaysNum);
            extension.setStatus(Status.GRANTED);
        } else {
            extension.setStatus(Status.REJECTED);
        }
        extensions.add(extension);

        return extension;
    }

    private double computeInterests(int extensionDaysNum) {
        int weeksNum = extensionDaysNum / NUM_DAYS_OF_WEEK;
        if (extensionDaysNum % NUM_DAYS_OF_WEEK > 0) {
            weeksNum++;
        }
        return interestPercentagePerDay * Math.pow(INTEREST_PER_DAY_EXTENSION_FACTOR, weeksNum);
    }

    public Long getContractId() {
        return id;
    }

    public void setContractId(Long contractId) {
        this.id = contractId;
    }

    public Integer getAmount() {
        return amount;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getInterestPercentagePerDay() {
        return interestPercentagePerDay;
    }

    public enum Status {
        NEW, GRANTED, REJECTED;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "contractId='" + id + '\'' +
                ", amount=" + amount +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", requestTime=" + requestTime +
                ", status=" + status +
                '}';
    }
}
