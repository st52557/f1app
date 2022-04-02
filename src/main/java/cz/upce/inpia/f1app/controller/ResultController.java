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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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
    public List<Result> getAllResults(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size, @RequestParam(required = false) String sort) {


        if(!sort.equals("ASC") && !sort.equals("DESC")){
            Page<Result> pageableResult = resultRepository.findAll(PageRequest.of(page, size));
            return pageableResult.getContent();
        }

        Page<Result> pageableResult = resultRepository.findAll(PageRequest.of(page,size, Sort.Direction.valueOf(sort),"points"));
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
    public ResponseEntity<?> createNewResult(@RequestBody Result newResult) {

        Result result = resultRepository.save(newResult);
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

}
