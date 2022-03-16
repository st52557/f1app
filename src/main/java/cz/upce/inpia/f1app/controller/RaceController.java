package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.repository.RaceRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/race")
@Api(tags = "races")
@CrossOrigin
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;

    @GetMapping(value = "/races")
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    @GetMapping(value = "/race/{id}")
    public Race getDriver(@PathVariable Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find race with id " + id));
    }

    @PostMapping(value = "/race")
    public ResponseEntity<?> createNewRace(@RequestBody Race newRace) {

        raceRepository.save(newRace);
        return ResponseEntity.ok("");
    }

    @DeleteMapping(value = "/race/{id}")
    public ResponseEntity<?> DeleteRace(@PathVariable Long id) {

        raceRepository.deleteById(id);
        return ResponseEntity.ok("");

    }

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
