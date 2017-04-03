package pl.study.loanapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.study.loanapp.Customer;
import pl.study.loanapp.Loan;
import pl.study.loanapp.LoanManager;
import pl.study.loanapp.repository.CustomerRepository;

import javax.xml.ws.Response;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RestController
public class LoanController {

    private final String CUSTOMER_BASE_URL = "customer/";


    private final LoanManager loanManager;

    @Autowired
    public LoanController(LoanManager loanManager) {
        this.loanManager = loanManager;
    }

    @PostMapping(CUSTOMER_BASE_URL)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer) {
        if (loanManager.saveCustomer(customer).isPresent()) {
            Long customerId = customer.getId();
            return ResponseEntity.created(URI.create(CUSTOMER_BASE_URL + customerId)).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping(CUSTOMER_BASE_URL + "{id}")
    public Loan applyForLoan(@PathVariable Long customerId, @RequestBody Loan loan) {
        return loanManager.applyForLoan(customerId, loan);
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return loanManager.getCustomers();
    }

    @GetMapping("/history")
    public Set<Loan> getHistory() {
        return Collections.emptySet();
    }
}
