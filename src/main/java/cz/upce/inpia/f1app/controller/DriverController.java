package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.CompareDriversDTO;
import cz.upce.inpia.f1app.dto.NewDriverDTO;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.services.DriverService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController(value = "/driver")
@Api(tags = "drivers")
@CrossOrigin
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @ApiOperation(value = "Method for getting all drivers")
    @GetMapping(value = "/drivers")
    public List<Driver> getAllDrivers(@RequestParam(required = false, defaultValue = "ASC") String sort) {

        return driverService.getDrivers(sort);
    }

    @ApiOperation(value = "Method for getting driver by id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/driver/{id}")
    public Driver getDriver(@PathVariable Long id) {

        return driverService.getDriverById(id);

    }

    @ApiOperation(value = "Method for creating new driver")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/driver")
    public ResponseEntity<Driver> createNewDriver(@Valid @RequestBody NewDriverDTO newDriver) {

        return driverService.saveNewDriver(newDriver);
    }

    @ApiOperation(value = "Method for deleting driver")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/driver/{id}")
    public ResponseEntity<Driver> deleteDriver(@PathVariable Long id) {

        return driverService.deleteDriverById(id);

    }

    @ApiOperation(value = "Method for editing driver")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/driver/{id}")
    public ResponseEntity<Driver> editDriver(@RequestBody NewDriverDTO newDriver, @PathVariable Long id) {

        return driverService.editDriverService(newDriver, id);
    }

    @ApiOperation(value = "Method for comparing two drivers")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/drivers-compare")
    public ResponseEntity<CompareDriversDTO> compareDrivers(Long firstDriverId, Long secondDriverId) {  // OK
        return ResponseEntity.ok(driverService.getCompareDrivers(firstDriverId, secondDriverId));

    }


}
