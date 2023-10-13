package rs.bajobozic.mapperdemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.bajobozic.mapperdemo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Optional<Customer> findCustomerByFirstName(String firstName);

    public List<Customer> findByAddress_HouseNumber(String houseNumber);
    // @Query("SELECT c FROM Customer c WHERE c.address.houseNumber = ?1")
    // public List<Customer> findByAddressHomeNumber(String houseNumber);
}
