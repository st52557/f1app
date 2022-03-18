package cz.upce.inpia.f1app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangePassDTO {

    private String oldPass;
    private String newPass;
    private String newPass2;

}
