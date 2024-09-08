package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightDto {

    private String flightCode;
    private String departure;
    private String arrival;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String price;
    private String itineraryId;
}
