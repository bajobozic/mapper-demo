package rs.bajobozic.mapperdemo.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CustomerDto(
                @NotNull @Size(min = 2, message = "First name must be at least 5 character long") String firstName,
                String lastName,
                AddressDto address,
                Department department,
                String date,
                List<CustomerItemDto> customerItems) {
}
