package rs.bajobozic.mapperdemo.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import rs.bajobozic.mapperdemo.dto.CustomerDto;
import rs.bajobozic.mapperdemo.entity.Customer;

@Mapper(uses = { AddressMapper.class, CustomerItemMapper.class }, imports = { String.class, LocalDate.class,
        DateTimeFormatter.class })
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(target = "date", expression = "java(customer.getCreatedAt().format(DateTimeFormatter.ofPattern(\"dd/MM/yyyy\")))")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "customerItems", target = "customerItems")
    CustomerDto convertToDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(target = "createdAt", expression = "java(LocalDate.now())")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "customerItems", target = "customerItems")
    Customer convertFromDto(CustomerDto customerDto);

    List<Customer> convertToCustomerCollection(List<CustomerDto> customerDtoList);

    @InheritInverseConfiguration
    List<CustomerDto> convertToCustomerDtoCollection(List<Customer> customerList);

    @InheritConfiguration
    void update(CustomerDto customerDto, @MappingTarget Customer customer);

    // Set logic here for every bidirectional @OneToOne, @OneToMany or @ManyToMany
    // associations update
    @AfterMapping
    default void updateAssociatedTables(CustomerDto customerDto, @MappingTarget Customer customer) {
        // init, update or delete association mappings for customer items
        if (customerDto.customerItems() != null) {
            customer.updateCustomerItems();
        } else {
            customer.removeCustomerItems();
        }

        // init, update or delete association mappings for addresses
        if (customerDto.address() != null) {
            customer.updateAddress();
        } else {
            customer.removeAddress();
        }
    }
}
