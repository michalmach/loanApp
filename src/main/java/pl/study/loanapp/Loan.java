package pl.study.loanapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    Loan(int amount, LocalDateTime fromDate, LocalDateTime toDate, LocalDateTime requestTime) {
        this.amount = amount;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.requestTime = requestTime;
        this.status = Status.NEW;
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
        NEW, GRANTED, REJECTED, PENDING;
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
