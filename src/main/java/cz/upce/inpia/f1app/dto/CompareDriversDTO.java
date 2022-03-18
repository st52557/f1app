package cz.upce.inpia.f1app.dto;

import cz.upce.inpia.f1app.dto.inner.Overtakes;
import cz.upce.inpia.f1app.dto.inner.PointsScored;
import cz.upce.inpia.f1app.dto.inner.RacesWon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompareDriversDTO {

    private Long firstDriverId;
    private Long secondDriverId;
    private PointsScored pointsScored;
    private RacesWon racesWon;
    private Overtakes overtakes;

}

