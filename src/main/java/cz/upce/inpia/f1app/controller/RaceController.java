package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.repository.RaceRepository;
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

    @ApiOperation(value = "Method getting all races")
    @GetMapping(value = "/races")
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    @ApiOperation(value = "Method for getting race by id")
    @GetMapping(value = "/race/{id}")
    public Race getDriver(@PathVariable Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find race with id " + id));
    }

    @ApiOperation(value = "Method for creating new race")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/race")
    public ResponseEntity<?> createNewRace(@RequestBody Race newRace) {

        raceRepository.save(newRace);
        return ResponseEntity.ok("");
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

        return raceRepository.findById(id)
                .map(race -> {
                    race.setRound(newRace.getRound());
                    race.setYear(newRace.getYear());
                    race.setCircuit(newRace.getCircuit());
                    raceRepository.save(race);
                    return ResponseEntity.ok("");
                })
                .orElseGet(() -> {
                    newRace.setId(id);
                    raceRepository.save(newRace);
                    return ResponseEntity.ok("");
                });
    }


}
