package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.DriverCumulativeSumPoints;
import cz.upce.inpia.f1app.dto.NewResultDTO;
import cz.upce.inpia.f1app.entity.Result;
import cz.upce.inpia.f1app.services.ResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/result")
@Api(tags = "results")
@CrossOrigin
public class ResultController {

    private final ResultService resultService;

    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @ApiOperation(value = "Method for getting all results")
    @GetMapping(value = "/results")
    public Page<Result> getAllResults(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "20") Integer size, @RequestParam(required = false, defaultValue = "ASC") String sort) {

        return resultService.getResults(page, size, sort);
    }
    
    
    @ApiOperation(value = "Method for getting result by id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/result/{id}")
    public Result getResult(@PathVariable Long id) {
        return resultService.getResultById(id);
    }

    @ApiOperation(value = "Method for creating new result")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/result")
    public ResponseEntity<Result> createNewResult(@RequestBody NewResultDTO newResultDTO) {

        return resultService.createResultService(newResultDTO);
    }


    @ApiOperation(value = "Method for deleting result by id")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/result/{id}")
    public ResponseEntity<Result> deleteResult(@PathVariable Long id) {

        return resultService.deleteResultService(id);

    }

    @ApiOperation(value = "Method for editing result")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/result/{id}")
    public ResponseEntity<Result> editResult(@RequestBody NewResultDTO newResult, @PathVariable Long id) {

        return resultService.editResultById(newResult, id);

    }
    

    @ApiOperation(value = "Method for getting all results by driver id")
    @GetMapping(value = "/results/{id}")
    public List<Result> getAllResultsByDriverId(@PathVariable Long id) {
        return resultService.getResultsByDriverId(id);
    }


    @ApiOperation(value = "Method for getting cumulative sum of points by driver id")
    @GetMapping(value = "/result/sum/{id}")
    public List<DriverCumulativeSumPoints> getCumulativePointsByDriverId(@PathVariable Long id) {

        return resultService.getDriverCumulativeSumPoints(id);

    }

}
