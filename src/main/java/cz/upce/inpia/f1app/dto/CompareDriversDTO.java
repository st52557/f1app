package cz.upce.inpia.f1app.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompareDriversDTO {

    private Long firstDriverId;
    private Long secondDriverId;

    @Getter
    @Setter
    @NoArgsConstructor
    public class PointsScored {
        private Double firstDriver;
        private Double secondDriver;
    }


}
