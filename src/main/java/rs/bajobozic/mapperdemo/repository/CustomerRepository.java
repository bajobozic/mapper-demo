package rs.bajobozic.mapperdemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.bajobozic.mapperdemo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Optional<Customer> findCustomerByFirstName(String firstName);
}
