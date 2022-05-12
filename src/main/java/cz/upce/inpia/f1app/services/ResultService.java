package cz.upce.inpia.f1app.services;

import cz.upce.inpia.f1app.dto.DriverCumulativeSumPoints;
import cz.upce.inpia.f1app.dto.NewResultDTO;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.entity.Result;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Collectors;

public class ResultService {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RaceRepository raceRepository;

    public ResponseEntity<String> getStringResponseEntity(Result newResult, Long id) {
        return resultRepository.findById(id)
                .map(result -> {
                    result.setRace(raceRepository.findById(newResult.getRace().getId()).orElse(null));
                    result.setDriver(driverRepository.findById(newResult.getDriver().getId()).orElse(null));
                    result.setPositionStart(newResult.getPositionStart());
                    result.setPositionFinal(newResult.getPositionFinal());
                    result.setFastestLapTime(newResult.getFastestLapTime());
                    result.setFastestLap(newResult.getFastestLap());
                    result.setMilisTime(newResult.getMilisTime());
                    result.setFastestTimeSpeed(newResult.getFastestTimeSpeed());
                    result.setPoints(newResult.getPoints());
                    resultRepository.save(result);
                    return ResponseEntity.ok("");
                })
                .orElseGet(() -> {
                    newResult.setId(id);
                    resultRepository.save(newResult);
                    return ResponseEntity.ok("");
                });
    }

    public ResponseEntity<Result> getResultResponseEntity(NewResultDTO newResultDTO) {
        Result resultToSave = new Result();

        Race race = raceRepository.findRaceById(newResultDTO.getRaceId());
        Driver driver = driverRepository.findDriverById(newResultDTO.getDriverId());

        resultToSave.setRace(race);
        resultToSave.setDriver(driver);
        resultToSave.setPoints(newResultDTO.getPoints());
        resultToSave.setFastestLap(newResultDTO.getFastestLap());
        resultToSave.setMilisTime(newResultDTO.getMilisTime());
        resultToSave.setFastestTimeSpeed(newResultDTO.getFastestTimeSpeed());
        resultToSave.setPositionFinal(newResultDTO.getPositionFinal());
        resultToSave.setPositionStart(newResultDTO.getPositionStart());


        Result result = resultRepository.save(resultToSave);
        return ResponseEntity.ok(result);
    }


    public List<DriverCumulativeSumPoints> getDriverCumulativeSumPoints(Long id) {
        List<Tuple> cumPoints = resultRepository.getCumulativePointsByDriverId(id);

        List<DriverCumulativeSumPoints> cumPointsDto = cumPoints.stream()
                .map(t -> new DriverCumulativeSumPoints(
                        t.get(0, Integer.class),
                        t.get(1, Integer.class),
                        t.get(2, Double.class),
                        t.get(3, Double.class)
                ))
                .collect(Collectors.toList());

        return cumPointsDto;
    }


}
