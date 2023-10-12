package rs.bajobozic.mapperdemo.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import rs.bajobozic.mapperdemo.dto.AddressDto;
import rs.bajobozic.mapperdemo.dto.CustomerDto;
import rs.bajobozic.mapperdemo.dto.CustomerItemDto;
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
    CustomerDto convert(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(target = "createdAt", expression = "java(LocalDate.now())")
    @Mapping(source = "department", target = "department")
    @Mapping(target = "address", ignore = true)
    @Mapping(source = "customerItems", target = "customerItems")
    Customer convertDto(CustomerDto customerDto);

    @InheritConfiguration
    void update(CustomerDto customerDto, @MappingTarget Customer customer);

    @AfterMapping
    default void updateAssociatedTables(CustomerDto customerDto, @MappingTarget Customer customer) {
        List<CustomerItemDto> customerItemsDtos = customerDto.getCustomerItems();
        AddressDto addressDto = customerDto.getAddress();

        // create or update addresses
        if (customerItemsDtos != null) {
            customer.updateCustomerItems();
        } else {
            customer.removeCustomerItems(customer.getCustomerItems());
        }

        // create or update addresses
        if (addressDto != null) {
            if (customer.getAddress() != null)
                AddressMapper.INSTANCE.updateAddress(addressDto, customer.getAddress());
            else
                customer.addAddress(AddressMapper.INSTANCE.convertDto(addressDto));
        } else {
            customer.removeAddress(customer.getAddress());
        }
    }
}
