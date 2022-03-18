package cz.upce.inpia.f1app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPassDTO {

    private String activationCode;
    private String email;
    private String password;

}
