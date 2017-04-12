package pl.study.loanapp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int amount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private LocalDateTime requestTime;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "customer", nullable = false)
    @JsonBackReference
    private Customer customer;

    Loan() {
        this.status = Status.NEW;
        this.requestTime = LocalDateTime.now();
    }

    Loan(int amount, LocalDateTime fromDate, LocalDateTime toDate) {
        this();
        this.amount = amount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Long getContractId() {
        return id;
    }

    public void setContractId(Long contractId) {
        this.id = contractId;
    }

    int getAmount() {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
