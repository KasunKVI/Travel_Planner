package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VenueDto {
    private String name;
    private String url;
    private String city;
    private String country;
    private String address;
    private double latitude;
    private double longitude;
}
