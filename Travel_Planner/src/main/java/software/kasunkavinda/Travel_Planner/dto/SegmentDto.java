package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentDto {
    private String departureAirport;
    private String departureTime;
    private String arrivalAirport;
    private String arrivalTime;
    private String carrierCode;
    private String aircraftCode;
    private int numberOfStops;
}
