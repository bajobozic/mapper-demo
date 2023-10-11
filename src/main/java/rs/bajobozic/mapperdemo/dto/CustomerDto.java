package rs.bajobozic.mapperdemo.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    @NotNull
    @Size(min = 2, message = "First name must be at least 5 character long")
    private String firstName;
    private String lastName;
    private AddressDto address;
    private Department department;
    private String date;
    private List<CustomerItemDto> customerItems;
}
