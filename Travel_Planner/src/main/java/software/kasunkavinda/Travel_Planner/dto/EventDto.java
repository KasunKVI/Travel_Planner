package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventDto {
    private String name;
    private String type;
    private String url;
    private String dateTime;
    private String status;
    private VenueDto venue;
    private List<ImageDto> images;
}

