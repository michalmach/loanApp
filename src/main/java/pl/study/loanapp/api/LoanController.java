package pl.study.loanapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.study.loanapp.Customer;
import pl.study.loanapp.Loan;
import pl.study.loanapp.LoanManager;
import pl.study.loanapp.repository.CustomerRepository;

import java.util.Set;

@RestController
public class LoanController {

    private final String CUSTOMER_BASE_URL = "customer/";

    @Autowired
    CustomerRepository customerRepository;

    @PostMapping("/customer")
    public String apply(@RequestBody Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);
        return CUSTOMER_BASE_URL + savedCustomer.getId();
    }

    @PostMapping("/customer/{id}")
    public Decision apply(@PathVariable Long id, @RequestBody Loan loan) {

        Customer customer = customerRepository.getOne(id);
        LoanManager.grantLoan(customer, loan);
    return Decision.ACCEPTED;
    }

    @GetMapping("/history")
    public Set<Loan> getHistory() {

    }
}
