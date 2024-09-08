package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private String location_id;
    private String name;
    private double distance;
    private String bearing;
    private String itineraryId;
    private AddressDto address_obj;
}

