package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.CompareDriversDTO;
import cz.upce.inpia.f1app.dto.inner.Overtakes;
import cz.upce.inpia.f1app.dto.inner.PointsScored;
import cz.upce.inpia.f1app.dto.inner.RacesWon;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import cz.upce.inpia.f1app.services.DriverService;
import cz.upce.inpia.f1app.services.ResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController(value = "/driver")
@Api(tags = "drivers")
@CrossOrigin
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @ApiOperation(value = "Method for getting all drivers")
    @GetMapping(value = "/drivers")
    public List<Driver> getAllDrivers(@RequestParam(required = false, defaultValue = "ASC") String sort) {

        if (!sort.equals("ASC") && !sort.equals("DESC")) {
            return driverRepository.findAll();
        }
        return driverRepository.findAll(Sort.by(Sort.Direction.valueOf(sort), "name"));
    }

    @ApiOperation(value = "Method for getting driver by id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/driver/{id}")
    public Driver getDriver(@PathVariable Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find driver with id " + id));
    }

    @ApiOperation(value = "Method for creating new driver")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/driver")
    public ResponseEntity<?> createNewDriver(@Valid @RequestBody Driver newDriver) {

        Driver driver = driverRepository.save(newDriver);

        return ResponseEntity.ok(driver);
    }

    @ApiOperation(value = "Method for deleting driver")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/driver/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long id) {

        driverRepository.deleteById(id);
        return ResponseEntity.ok("");

    }

    @ApiOperation(value = "Method for editing driver")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/driver/{id}")
    public ResponseEntity<?> editDriver(@RequestBody Driver newDriver, @PathVariable Long id) {

        return driverService.getStringResponseEntity(newDriver, id);
    }

    @ApiOperation(value = "Method for comparing two drivers")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/drivers-compare")
    public CompareDriversDTO compareDrivers(Long firstDriverId, Long secondDriverId) {
        return driverService.getCompareDriversDTO(firstDriverId, secondDriverId);

    }


}
