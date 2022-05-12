package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.services.DriverService;
import cz.upce.inpia.f1app.services.RaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/race")
@Api(tags = "races")
@CrossOrigin
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @ApiOperation(value = "Method getting all races")
    @GetMapping(value = "/races")
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    @ApiOperation(value = "Method for getting race by id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/race/{id}")
    public Race getDriver(@PathVariable Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find race with id " + id));
    }

    @ApiOperation(value = "Method for creating new race")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/race")
    public ResponseEntity<?> createNewRace(@RequestBody Race newRace) {

        Race race = raceRepository.save(newRace);
        return ResponseEntity.ok(race);
    }

    @ApiOperation(value = "Method for deleting race by id")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/race/{id}")
    public ResponseEntity<?> DeleteRace(@PathVariable Long id) {

        raceRepository.deleteById(id);
        return ResponseEntity.ok("");

    }

    @ApiOperation(value = "Method for editing race")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/race/{id}")
    public ResponseEntity<?> editRace(@RequestBody Race newRace, @PathVariable Long id) {

        return raceService.getStringResponseEntity(newRace, id);
    }


}
