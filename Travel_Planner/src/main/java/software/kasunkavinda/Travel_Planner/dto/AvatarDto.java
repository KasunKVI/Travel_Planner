package software.kasunkavinda.Travel_Planner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvatarDto {
    private String thumbnail;
    private String small;
    private String medium;
    private String large;
    private String original;
}
