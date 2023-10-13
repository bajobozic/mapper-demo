package rs.bajobozic.mapperdemo.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import rs.bajobozic.mapperdemo.dto.AddressDto;
import rs.bajobozic.mapperdemo.entity.Address;

@Mapper(imports = { String.class, Integer.class })
public interface AddressMapper {
    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    @Mapping(target = "number", expression = "java(Integer.parseInt( address.getHouseNumber()))")
    @Mapping(source = "cityName", target = "city")
    AddressDto convertToDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "houseNumber", expression = "java(String.valueOf( addressDto.getNumber()))")
    @Mapping(source = "city", target = "cityName")
    @Mapping(target = "customer", ignore = true)
    Address convertFromDto(AddressDto addressDto);

    @InheritConfiguration
    void updateAddress(AddressDto addressDto, @MappingTarget Address address);
}