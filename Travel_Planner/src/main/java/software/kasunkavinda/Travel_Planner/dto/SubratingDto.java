package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubratingDto {

    private String name;
    private String ratingImageUrl;
    private int value;
    private String localizedName;


}
