package software.kasunkavinda.Travel_Planner.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItinerariesDto {

    private String id;
    private String origin;
    private String destination;
    private String startDate;
    private String endDate;
    private String description;
    private boolean isInternal;
    private String status;
    private List<FlightDto> flights;
    private List<HotelDto> hotels;
}

