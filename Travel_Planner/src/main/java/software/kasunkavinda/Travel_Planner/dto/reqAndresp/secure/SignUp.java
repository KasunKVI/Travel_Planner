package software.kasunkavinda.Travel_Planner.dto.reqAndresp.secure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignUp {

    private String username;
    private String country;
    private String email;
    private String password;


}
