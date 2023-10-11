package rs.bajobozic.mapperdemo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
}
