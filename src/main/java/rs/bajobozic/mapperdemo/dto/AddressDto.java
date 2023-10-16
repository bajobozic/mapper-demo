package rs.bajobozic.mapperdemo.dto;

public record AddressDto(
                String street,
                Integer number,
                Integer zip,
                String city) {
}
