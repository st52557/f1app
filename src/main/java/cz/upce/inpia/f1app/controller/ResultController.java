package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.NewResultDTO;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.entity.Result;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    @ApiOperation(value = "Method for getting result by id")
    @GetMapping(value = "/result/{id}")
    public Result getResult(@PathVariable Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find result with id " + id));
    }

    @ApiOperation(value = "Method for creating new result")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/result")
    public ResponseEntity<?> createNewResult(@RequestBody Result newResult) {

        resultRepository.save(newResult);
        return ResponseEntity.ok("");
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

}
