package pl.study.loanapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.study.loanapp.Customer;
import pl.study.loanapp.Loan;
import pl.study.loanapp.LoanManager;
import pl.study.loanapp.repository.CustomerRepository;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

@RestController
public class LoanController {

    private final String CUSTOMER_BASE_URL = "customer/";

    private final CustomerRepository customerRepository;
    private final LoanManager loanManager;

    @Autowired
    public LoanController(CustomerRepository customerRepository, LoanManager loanManager) {
        this.customerRepository = customerRepository;
        this.loanManager = loanManager;
    }

    @PostMapping("/customer")
    public ResponseEntity<Void> apply(@RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseEntity.created(URI.create(CUSTOMER_BASE_URL + savedCustomer.getId())).build(); //NOT HATEOAS
        //after changes - still not hateoas
    }

    @PostMapping("/customer/{id}")
    public Decision apply(@PathVariable Long id, @RequestBody Loan loan) {

        Customer customer = customerRepository.getOne(id);
        loanManager.grantLoan(customer, loan);
    return Decision.ACCEPTED;
    }

    @GetMapping("/history")
    public Set<Loan> getHistory() {
        return Collections.emptySet();
    }
}
