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
    void update(CustomerDto homeDto, @MappingTarget Customer home);

    @AfterMapping
    default void updateJpaRelations(CustomerDto customerDto, @MappingTarget Customer customer) {
        List<CustomerItemDto> customerItemsDtos = customerDto.getCustomerItems();
        AddressDto addressDto = customerDto.getAddress();

        customerItemsDtos
                .stream()
                .map(dto -> CustomerItemMapper.INSTANCE.convert(dto))
                .toList()
                .forEach(item -> customer.addCustomerItem(item));
        customer.addAddress(AddressMapper.INSTANCE.convertDto(addressDto));
    }
}
