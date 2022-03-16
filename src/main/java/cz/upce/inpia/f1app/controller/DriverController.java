package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.CompareDriversDTO;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/driver")
@Api(tags = "drivers")
@CrossOrigin
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ResultRepository resultRepository;

    @GetMapping(value = "/drivers")
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @GetMapping(value = "/driver/{id}")
    public Driver getDriver(@PathVariable Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find driver with id " + id));
    }


    @PostMapping(value = "/driver")
    public ResponseEntity<?> createNewDriver(@RequestBody Driver newDriver) {

        driverRepository.save(newDriver);
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/driver/{id}")
    public ResponseEntity<?> DeleteDriver(@PathVariable Long id) {

        driverRepository.deleteById(id);
        return ResponseEntity.ok("");

    }

    @PutMapping(value = "/driver/{id}")
    public ResponseEntity<?> editDriver(@RequestBody Driver newDriver, @PathVariable Long id) {

        return driverRepository.findById(id)
                .map(driver -> {
                    driver.setName(newDriver.getName());
                    driver.setCode(newDriver.getCode());
                    driverRepository.save(driver);
                    return ResponseEntity.ok("");
                })
                .orElseGet(() -> {
                    newDriver.setId(id);
                    driverRepository.save(newDriver);
                    return ResponseEntity.ok("");
                });
    }

    @GetMapping(value = "/drivers-compare")
    public CompareDriversDTO compareDrivers(Long firstDriverId, Long secondDriverId) {
        CompareDriversDTO compareDriversDTO = new CompareDriversDTO();

        compareDriversDTO.setSecondDriverId(secondDriverId);
        compareDriversDTO.setFirstDriverId(firstDriverId);

        Driver firstDriver = driverRepository.findById(firstDriverId).orElse(null);
        Driver secondDriver = driverRepository.findById(secondDriverId).orElse(null);

        CompareDriversDTO.PointsScored pointsScored = compareDriversDTO.new PointsScored();

        assert firstDriver != null;
        pointsScored.setFirstDriver(resultRepository.countAllPointsByDriverId(firstDriver.getId()));
        assert secondDriver != null;
        pointsScored.setFirstDriver(resultRepository.countAllPointsByDriverId(secondDriver.getId()));

        return compareDriversDTO;

    }


}
