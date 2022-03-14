package cz.upce.inpia.f1app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Result {

    @Id
    private Long result_id;

    private int positionFinal;
    private int positionOrder;
    private int points;
    private int laps;
    private int milisTime;
    private int fastestLap;
    private int fastestLapTime;
    private int fastestTimeSpeed;

}
