package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightOfferDto {
    private String id;
    private String lastTicketingDate;
    private int numberOfBookableSeats;
    private PriceDto price;
    private List<ItineraryDto> itineraries;
}
