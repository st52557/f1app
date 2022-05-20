package cz.upce.inpia.f1app.services;

import cz.upce.inpia.f1app.dto.DriverCumulativeSumPoints;
import cz.upce.inpia.f1app.dto.NewResultDTO;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.entity.Result;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final ResultRepository resultRepository;
    private final DriverRepository driverRepository;
    private final RaceRepository raceRepository;

    public ResultService(ResultRepository resultRepository, DriverRepository driverRepository, RaceRepository raceRepository) {
        this.resultRepository = resultRepository;
        this.driverRepository = driverRepository;
        this.raceRepository = raceRepository;
    }


    public Page<Result> getResults(Integer page, Integer size, String sort) {
        if (!sort.equals("ASC") && !sort.equals("DESC")) {
            return resultRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "points"));
        }

        return resultRepository.findAll(PageRequest.of(page, size, Sort.Direction.valueOf(sort), "points"));
    }

    public Result getResultById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find result with id " + id));
    }

    public ResponseEntity<Result> createResultService(NewResultDTO newResult) {

        Result result = new Result();

        result.setRace(raceRepository.findRaceById(newResult.getRaceId()));
        result.setDriver(driverRepository.findDriverById(newResult.getDriverId()));
        result.setPositionStart(newResult.getPositionStart());
        result.setPositionFinal(newResult.getPositionFinal());
        result.setFastestLap(newResult.getFastestLap());
        result.setMilisTime(newResult.getMilisTime());
        result.setFastestTimeSpeed(newResult.getFastestTimeSpeed());
        result.setPoints(newResult.getPoints());

        return ResponseEntity.ok(resultRepository.save(result));

    }

    public ResponseEntity<Result> deleteResultService(Long id) {
        Result result = resultRepository.findResultById(id);
        resultRepository.deleteById(id);
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<Result> editResultById(NewResultDTO newResultDTO, Long id) {

        Result resultToSave = resultRepository.findResultById(id);

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

    public List<Result> getResultsByDriverId(Long id) {
        return resultRepository.findAllByDriverId(id);
    }

    public List<DriverCumulativeSumPoints> getDriverCumulativeSumPoints(Long id) {
        List<Tuple> cumPoints = resultRepository.getCumulativePointsByDriverId(id);

        return cumPoints.stream()
                .map(t -> new DriverCumulativeSumPoints(
                        t.get(0, Integer.class),
                        t.get(1, Integer.class),
                        t.get(2, Double.class),
                        t.get(3, Double.class)
                ))
                .collect(Collectors.toList());


    }


}
