package pl.study.loanapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.study.loanapp.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

   //TODO Customer findByName(String name);


}
