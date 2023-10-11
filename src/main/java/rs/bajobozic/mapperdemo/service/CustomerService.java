package rs.bajobozic.mapperdemo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rs.bajobozic.mapperdemo.dto.AddressDto;
import rs.bajobozic.mapperdemo.dto.CustomerDto;
import rs.bajobozic.mapperdemo.dto.CustomerItemDto;
import rs.bajobozic.mapperdemo.entity.Customer;
import rs.bajobozic.mapperdemo.exception.CustomerNotFoundException;
import rs.bajobozic.mapperdemo.mapper.AddressMapper;
import rs.bajobozic.mapperdemo.mapper.CustomerItemMapper;
import rs.bajobozic.mapperdemo.mapper.CustomerMapper;
import rs.bajobozic.mapperdemo.repository.CustomerRepository;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<CustomerDto> getAllCustomers() {
        var customers = customerRepository.findAll().stream().map(customer -> CustomerMapper.INSTANCE.convert(customer))
                .toList();
        return customers;
    }

    public CustomerDto getCustomerByFirstName(String firstName) {
        Customer customer = customerRepository.findCustomerByFirstName(firstName)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with first name not found", firstName));
        return CustomerMapper.INSTANCE.convert(customer);
    }

    @Transactional
    public void createCustomer(CustomerDto customerDto) {
        var dbCustomer = customerRepository.findCustomerByFirstName(customerDto.getFirstName());
        if (dbCustomer.isPresent())
            throw new IllegalArgumentException(
                    "Customer with first name " + customerDto.getFirstName() + " already exist");

        List<CustomerItemDto> customerItemsDtos = customerDto.getCustomerItems();
        AddressDto addressDto = customerDto.getAddress();
        Customer customer = CustomerMapper.INSTANCE.convertDto(customerDto);

        customerItemsDtos
                .stream()
                .map(dto -> CustomerItemMapper.INSTANCE.convert(dto))
                .toList()
                .forEach(item -> customer.addCustomerItem(item));
        customer.addAddress(AddressMapper.INSTANCE.convertDto(addressDto));
        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(String firstName) {
        var customer = customerRepository.findCustomerByFirstName(firstName);
        if (customer.isPresent())
            customerRepository.delete(customer.get());
    }

    @Transactional
    public CustomerDto updateCustomer(String firstName, CustomerDto customerDto) {
        var customer = customerRepository.findCustomerByFirstName(firstName)
                .orElseThrow(
                        () -> new IllegalArgumentException("Customer with first name: " + firstName + " not found"));
        CustomerMapper.INSTANCE.update(customerDto, customer);
        customerRepository.save(customer);
        return CustomerMapper.INSTANCE.convert(customer);
    }

    @Transactional
    public CustomerDto updateCustomerDepartment(String firstName, String department) {
        var customer = customerRepository.findCustomerByFirstName(firstName)
                .orElseThrow(
                        () -> new IllegalArgumentException("Customer with first name: " + firstName + " not found"));
        customer.setDepartment(department);
        customerRepository.save(customer);
        return CustomerMapper.INSTANCE.convert(customer);
    }
}
