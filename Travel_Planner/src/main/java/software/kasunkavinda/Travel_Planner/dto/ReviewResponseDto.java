package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewResponseDto {

    private long id;
    private String language;
    private long locationId;
    private String publishedDate;
    private int rating;
    private String ratingImageUrl;
    private String url;
    private String text;
    private String title;
    private String tripType;
    private String travelDate;
    private UserDto user;
    private Map<String, SubratingDto> subratings;
    private OwnerResponseDto ownerResponse;

}
