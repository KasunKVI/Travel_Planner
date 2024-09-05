package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDetailsDto {
    private String origin;
    private String destination;
    private String departureDate;
    private int adults;
    private int max;
}

