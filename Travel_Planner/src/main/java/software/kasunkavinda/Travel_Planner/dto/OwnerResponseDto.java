package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OwnerResponseDto {
    private long id;
    private String title;
    private String text;
    private String language;
    private String author;
    private String publishedDate;

}
