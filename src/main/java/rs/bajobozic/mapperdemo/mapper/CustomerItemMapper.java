package rs.bajobozic.mapperdemo.mapper;

import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import rs.bajobozic.mapperdemo.dto.CustomerItemDto;
import rs.bajobozic.mapperdemo.entity.CustomerItem;

@Mapper
public interface CustomerItemMapper {
    CustomerItemMapper INSTANCE = Mappers.getMapper(CustomerItemMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "item", target = "item")
    @Mapping(target = "customer", ignore = true)
    CustomerItem convert(CustomerItemDto customerItemDto);

    @Mapping(source = "item", target = "item")
    CustomerItemDto covertToDto(CustomerItem customerItem);

    List<CustomerItem> convertToCustomerItemCollection(List<CustomerItemDto> customerItemList);

    @InheritConfiguration
    void updateCustomerItem(CustomerItemDto customerItemDto, @MappingTarget CustomerItem customerItem);

    @InheritConfiguration
    void updateCustomerItemList(List<CustomerItemDto> customerItemDtoList,
            @MappingTarget List<CustomerItem> customerItemList);
}
