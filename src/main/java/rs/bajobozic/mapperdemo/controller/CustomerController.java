package rs.bajobozic.mapperdemo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.bajobozic.mapperdemo.dto.CustomerDto;
import rs.bajobozic.mapperdemo.service.CustomerService;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @GetMapping(path = "/{firstName}")
    public ResponseEntity<CustomerDto> getCustomerByFirstName(@PathVariable String firstName) {
        CustomerDto customerDto = customerService.getCustomerByFirstName(firstName);
        return ResponseEntity.ok().body(customerDto);
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@Validated @RequestBody CustomerDto customerDto) {
        customerService.createCustomer(customerDto);
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> updateCustomer(@RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok().body(customerService.updateCustomer(customerDto));
    }

    @PatchMapping(path = "/updateDepartment/{firstName}/{department}")
    public ResponseEntity<CustomerDto> updateCustomerDepartment(@PathVariable(name = "firstName") String firstName,
            @PathVariable(name = "department") String department) {
        return ResponseEntity.ok().body(customerService.updateCustomerDepartment(firstName, department));
    }

    @DeleteMapping(path = "/delete/{firstName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable(name = "firstName") String firstName) {
        customerService.deleteCustomer(firstName);
    }

}
