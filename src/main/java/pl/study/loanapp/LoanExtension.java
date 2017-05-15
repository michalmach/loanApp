package pl.study.loanapp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class LoanExtension {

    private int extensionDaysNum;
    LocalDateTime requestTime;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan", nullable = false)
    @JsonBackReference
    private Loan loan;
    private Loan.Status status;

    LoanExtension() {
        this.status = Loan.Status.NEW;
        this.requestTime = LocalDateTime.now();
    }

    public LoanExtension(int extensionDaysNum) {
        this();
        this.extensionDaysNum = extensionDaysNum;
    }

    public int getExtensionDaysNum() {
        return extensionDaysNum;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public Long getId() {
        return id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Loan.Status getStatus() {
        return status;
    }

    public void setStatus(Loan.Status status) {
        this.status = status;
    }
}
