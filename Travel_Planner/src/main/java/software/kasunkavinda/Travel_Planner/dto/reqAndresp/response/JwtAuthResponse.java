package software.kasunkavinda.Travel_Planner.dto.reqAndresp.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JwtAuthResponse {
    private String token;
}
