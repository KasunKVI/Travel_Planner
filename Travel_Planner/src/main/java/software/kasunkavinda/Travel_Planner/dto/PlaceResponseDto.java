package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PlaceResponseDto {

    private String name;
    private String address;
    private String locality;
    private String region;
    private String country;
    private String postcode;
    private double latitude;
    private double longitude;
    private int distance;
    private String category;

}
