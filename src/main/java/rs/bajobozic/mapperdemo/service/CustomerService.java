package rs.bajobozic.mapperdemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rs.bajobozic.mapperdemo.dto.AddressDto;
import rs.bajobozic.mapperdemo.dto.CustomerDto;
import rs.bajobozic.mapperdemo.entity.Customer;
import rs.bajobozic.mapperdemo.exception.CustomerNotFoundException;
import rs.bajobozic.mapperdemo.mapper.AddressMapper;
import rs.bajobozic.mapperdemo.mapper.CustomerMapper;
import rs.bajobozic.mapperdemo.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<CustomerDto> getAllCustomers() {
        var customers = customerRepository.findAll().stream()
                .map(customer -> CustomerMapper.INSTANCE.convertToDto(customer))
                .toList();
        return customers;
    }

    public CustomerDto getCustomerByFirstName(String firstName) {
        Customer customer = customerRepository.findCustomerByFirstName(firstName)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with first name not found", firstName));
        return CustomerMapper.INSTANCE.convertToDto(customer);
    }

    @Transactional
    public void createCustomer(CustomerDto customerDto) {
        var dbCustomer = customerRepository.findCustomerByFirstName(customerDto.getFirstName());
        if (dbCustomer.isPresent())
            throw new IllegalArgumentException(
                    "Customer with first name " + customerDto.getFirstName() + " already exist");
        customerRepository.save(CustomerMapper.INSTANCE.convertFromDto(customerDto));
    }

    @Transactional
    public void deleteCustomer(String firstName) {
        var customer = customerRepository.findCustomerByFirstName(firstName);
        if (customer.isPresent())
            customerRepository.delete(customer.get());
    }

    @Transactional
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        var customer = customerRepository.findCustomerByFirstName(customerDto.getFirstName())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Customer with first name: " + customerDto.getFirstName() + " not found"));
        CustomerMapper.INSTANCE.update(customerDto, customer);
        customerRepository.save(customer);
        return customerDto;
    }

    @Transactional
    public CustomerDto updateCustomerDepartment(String firstName, String department) {
        var customer = customerRepository.findCustomerByFirstName(firstName)
                .orElseThrow(
                        () -> new IllegalArgumentException("Customer with first name: " + firstName + " not found"));
        customer.setDepartment(department);
        customerRepository.save(customer);
        return CustomerMapper.INSTANCE.convertToDto(customer);
    }

    @Transactional
    public AddressDto updateAddress(String id, AddressDto addressDto) {
        var customer = customerRepository.findCustomerByFirstName(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Customer with first name: " + id + " not found"));
        AddressMapper.INSTANCE.updateAddress(addressDto, customer.getAddress());
        customerRepository.save(customer);
        return addressDto;
    }

    public List<CustomerDto> getAllCustomersWithHomeNumber(String houseNumber) {
        var customerList = customerRepository.findByAddress_HouseNumber(houseNumber);
        return CustomerMapper.INSTANCE.convertToCustomerDtoCollection(customerList);
    }

    public List<AddressDto> getAllAddressesForHomeNumber(String houseNumber) {
        var addressesList = customerRepository.findByAddressHomeNumber(houseNumber);
        return AddressMapper.INSTANCE.convertToDtoCollection(addressesList);
    }

    @Transactional
    public void deleteAll() {
        customerRepository.deleteAll();
    }
}
