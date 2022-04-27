package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.DriverCumulativeSumPoints;
import cz.upce.inpia.f1app.dto.NewResultDTO;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.entity.Result;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController(value = "/result")
@Api(tags = "results")
@CrossOrigin
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private RaceRepository raceRepository;

    @ApiOperation(value = "Method for getting all results")
    @GetMapping(value = "/results")
    public List<Result> getAllResults(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false, defaultValue = "ASC") String sort) {

        if (!sort.equals("ASC") && !sort.equals("DESC")) {
            return resultRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "points")).getContent();
        }

        if (page == null || size == null) {
            return resultRepository.findAll(Sort.by(Sort.Direction.valueOf(sort), "points"));
        }

        Page<Result> pageableResult = resultRepository.findAll(PageRequest.of(page, size, Sort.Direction.valueOf(sort), "points"));
        return pageableResult.getContent();
    }


    @ApiOperation(value = "Method for getting result by id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/result/{id}")
    public Result getResult(@PathVariable Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find result with id " + id));
    }

    @ApiOperation(value = "Method for creating new result")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/result")
    public ResponseEntity<?> createNewResult(@RequestBody NewResultDTO newResultDTO) {

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

    @ApiOperation(value = "Method for deleting result by id")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/result/{id}")
    public ResponseEntity<?> DeleteResult(@PathVariable Long id) {

        resultRepository.deleteById(id);
        return ResponseEntity.ok("");

    }

    @ApiOperation(value = "Method for editing result")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/result/{id}")
    public ResponseEntity<?> editResult(@RequestBody Result newResult, @PathVariable Long id) {

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

    @ApiOperation(value = "Method for getting all results by driver id")
    @GetMapping(value = "/results/{id}")
    public List<Result> getAllResultsByDriverId(@PathVariable Long id) {
        return resultRepository.findAllByDriverId(id);
    }

    @ApiOperation(value = "Method for getting cumulative sum of points by driver id")
    @GetMapping(value = "/result/sum/{id}")
    public List<DriverCumulativeSumPoints> getCumulativePointsByDriverId(@PathVariable Long id) {

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
