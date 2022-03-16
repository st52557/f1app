package cz.upce.inpia.f1app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewResultDTO {

    private int id;

    private int driverId;
    private int raceId;

    private int positionFinal;
    private int positionOrder;
    private int positionStart;
    private Double points;
    private int laps;
    private int milisTime;
    private int fastestLap;
    private int fastestLapTime;
    private int fastestTimeSpeed;

}
