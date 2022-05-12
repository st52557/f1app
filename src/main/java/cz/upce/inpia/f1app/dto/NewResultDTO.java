package cz.upce.inpia.f1app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewResultDTO {

    private Long id;

    private Long driverId;
    private Long raceId;

    private int positionFinal = 0;
    private int positionOrder = 0;
    private int positionStart = 0;
    private Double points = 0.0;
    private int laps = 0;
    private int milisTime = 0;
    private int fastestLap = 0;
    private int fastestLapTime = 0;
    private Double fastestTimeSpeed = 0.0;

}
