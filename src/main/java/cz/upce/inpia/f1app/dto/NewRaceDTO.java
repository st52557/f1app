package cz.upce.inpia.f1app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewRaceDTO {

    private int id;
    private int year;
    private int round;
    private String circuit;
}
