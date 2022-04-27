package cz.upce.inpia.f1app.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
public class DriverCumulativeSumPoints {

    private Integer round;
    private Integer year;
    private Double points;
    private Double cumPoints;


    public DriverCumulativeSumPoints(Integer round, Integer year,Double points, Double cumPoints) {
        this.round = round;
        this.year = year;
        this.points = points;
        this.cumPoints = cumPoints;
    }
}
