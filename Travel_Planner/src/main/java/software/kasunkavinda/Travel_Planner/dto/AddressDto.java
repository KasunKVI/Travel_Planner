package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String country;
    private String postalcode;
    private String address_string;
}
