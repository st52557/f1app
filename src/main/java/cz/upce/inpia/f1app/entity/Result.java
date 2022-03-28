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

    private Integer positionFinal;
    private Integer positionOrder;
    private Integer positionStart;
    private Double points;
    private Integer laps;
    private Integer milisTime;
    private Integer fastestLap;
    private String fastestLapTime;
    private Double fastestTimeSpeed;

}
