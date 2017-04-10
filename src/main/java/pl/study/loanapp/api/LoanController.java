package pl.study.loanapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.study.loanapp.Customer;
import pl.study.loanapp.Loan;
import pl.study.loanapp.LoanManager;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

        Optional<Customer> optionalCustomer = loanManager.saveCustomer(customer);
        if (optionalCustomer.isPresent()) {
            Long customerId = optionalCustomer.get().getId();
            return ResponseEntity.created(URI.create(CUSTOMER_BASE_URL + customerId)).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PostMapping(CUSTOMER_BASE_URL + "{customerId}")
    public Loan applyForLoan(@PathVariable Long customerId, @RequestBody Loan loan) {
        return loanManager.applyForLoan(customerId, loan);
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return loanManager.getCustomers();
    }

    @GetMapping(CUSTOMER_BASE_URL + "{customerId}" + "/history")
    public Set<Loan> getHistory(@PathVariable Long customerId) {
        return loanManager.retrieveHistory(customerId);
    }

    @GetMapping("/date")
    public KlasaDoWykorzystaniaTeraz getLocalDateTimeNow (){
        return new KlasaDoWykorzystaniaTeraz(LocalDateTime.now());
    }

    private class KlasaDoWykorzystaniaTeraz {
        private LocalDateTime ltd;
        public KlasaDoWykorzystaniaTeraz(LocalDateTime ltd) {
            this.ltd = ltd;
        }

        public LocalDateTime getLtd() {
            return ltd;
        }
    }
}
