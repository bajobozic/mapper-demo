package rs.bajobozic.mapperdemo.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ListIterator;

import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.util.Pair;

import rs.bajobozic.mapperdemo.dto.AddressDto;
import rs.bajobozic.mapperdemo.dto.CustomerDto;
import rs.bajobozic.mapperdemo.dto.CustomerItemDto;
import rs.bajobozic.mapperdemo.entity.Customer;
import rs.bajobozic.mapperdemo.entity.CustomerItem;

@Mapper(uses = { AddressMapper.class, CustomerItemMapper.class }, imports = { String.class, LocalDate.class,
        DateTimeFormatter.class })
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(target = "date", expression = "java(home.getCreatedAt().format(DateTimeFormatter.ofPattern(\"dd/MM/yyyy\")))")
    @Mapping(source = "department", target = "department")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "customerItems", target = "customerItems")
    CustomerDto convert(Customer home);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(target = "createdAt", expression = "java(LocalDate.now())")
    @Mapping(source = "department", target = "department")
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "customerItems", ignore = true)
    Customer convertDto(CustomerDto homeDto);

    @InheritConfiguration
    void update(CustomerDto customerDto, @MappingTarget Customer customer);

    @AfterMapping
    default void updateJpaRelations(CustomerDto customerDto, @MappingTarget Customer customer) {
        List<CustomerItemDto> customerItemsDtos = customerDto.getCustomerItems();
        AddressDto addressDto = customerDto.getAddress();

        // create or update addresses
        if (customerItemsDtos != null) {
            if (customer.getCustomerItems() != null && !customer.getCustomerItems().isEmpty()) {
                customerItemsDtos
                        .stream()
                        .map(dto -> Pair.of(dto, CustomerItemMapper.INSTANCE.convert(dto)))
                        .toList()
                        .forEach(p -> CustomerItemMapper.INSTANCE.updateCustomerItem(p.getFirst(),
                                p.getSecond()));
            } else {
                var list = customerItemsDtos
                        .stream()
                        .map(dto -> CustomerItemMapper.INSTANCE.convert(dto))
                        .toList();
                customer.addCustomerItems(list);

            }
        } else {
            ListIterator<CustomerItem> listIterator = customer.getCustomerItems().listIterator();
            customer.removeCustomerItems(listIterator);
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
