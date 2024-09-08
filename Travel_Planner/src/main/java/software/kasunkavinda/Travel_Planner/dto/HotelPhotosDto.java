package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HotelPhotosDto {
    private long id;
    private String caption;
    private String publishedDate;
    private String size;
    private String url;


}
