package cz.upce.inpia.f1app.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private String password;

    private String email;

    private String name;

}
