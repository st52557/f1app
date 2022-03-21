package cz.upce.inpia.f1app.dto;

import cz.upce.inpia.f1app.jwt.JwtResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginRetDTO {

    private String token;
    private boolean isAdmin;

}
