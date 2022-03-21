package cz.upce.inpia.f1app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Driver driver;

    @ManyToOne
    private Race race;

    private int positionFinal;
    private int positionOrder;
    private int positionStart;
    private Double points;
    private int laps;
    private int milisTime;
    private int fastestLap;
    private Date fastestLapTime;
    private Double fastestTimeSpeed;

}
