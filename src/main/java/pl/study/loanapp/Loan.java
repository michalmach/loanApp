package pl.study.loanapp;

import java.time.LocalDateTime;

public class Loan {

    private String contractId;
    private int amount;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String ipAddress;
    private LocalDateTime requestTime;
    private Status status;

    public Loan(String contractId, int amount, LocalDateTime fromDate, LocalDateTime toDate, String ipAddress) {
        this.contractId = contractId;
        this.amount = amount;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.ipAddress = ipAddress;

        this.requestTime = LocalDateTime.now();
        this.status = Status.NEW;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public int getAmount() {
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

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public enum Status {
        NEW, GRANTED, REJECTED, PENDING;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "contractId='" + contractId + '\'' +
                ", amount=" + amount +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", ipAddress='" + ipAddress + '\'' +
                ", requestTime=" + requestTime +
                ", status=" + status +
                '}';
    }
}
