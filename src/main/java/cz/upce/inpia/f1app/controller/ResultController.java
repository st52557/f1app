package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.DriverCumulativeSumPoints;
import cz.upce.inpia.f1app.dto.NewResultDTO;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.entity.Result;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import cz.upce.inpia.f1app.services.ResultService;
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

@RestController(value = "/result")
@Api(tags = "results")
@CrossOrigin
public class ResultController {

    private final ResultRepository resultRepository;

    private final ResultService resultService;

    public ResultController(ResultService resultService, ResultRepository resultRepository) {
        this.resultService = resultService;
        this.resultRepository = resultRepository;
    }

    @ApiOperation(value = "Method for getting all results")
    @GetMapping(value = "/results")
    public Page<Result> getAllResults(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "20") Integer size, @RequestParam(required = false, defaultValue = "ASC") String sort) {

        if (!sort.equals("ASC") && !sort.equals("DESC")) {
            return resultRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "points"));
        }

        return resultRepository.findAll(PageRequest.of(page, size, Sort.Direction.valueOf(sort), "points"));
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

        return resultService.getResultResponseEntity(newResultDTO);
    }
    

    @ApiOperation(value = "Method for deleting result by id")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/result/{id}")
    public ResponseEntity<?> deleteResult(@PathVariable Long id) {

        resultRepository.deleteById(id);
        return ResponseEntity.ok("");

    }

    @ApiOperation(value = "Method for editing result")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/result/{id}")
    public ResponseEntity<?> editResult(@RequestBody Result newResult, @PathVariable Long id) {

        return resultService.getStringResponseEntity(newResult, id);

    }
    

    @ApiOperation(value = "Method for getting all results by driver id")
    @GetMapping(value = "/results/{id}")
    public List<Result> getAllResultsByDriverId(@PathVariable Long id) {
        return resultRepository.findAllByDriverId(id);
    }

    @ApiOperation(value = "Method for getting cumulative sum of points by driver id")
    @GetMapping(value = "/result/sum/{id}")
    public List<DriverCumulativeSumPoints> getCumulativePointsByDriverId(@PathVariable Long id) {

        return resultService.getDriverCumulativeSumPoints(id);

    }

}
