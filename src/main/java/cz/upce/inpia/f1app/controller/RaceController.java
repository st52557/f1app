package cz.upce.inpia.f1app.controller;

import cz.upce.inpia.f1app.dto.NewRaceDTO;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.services.RaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController(value = "/race")
@Api(tags = "races")
@CrossOrigin
public class RaceController {

    private final RaceService raceService;

    public RaceController(RaceService raceService) {
        this.raceService = raceService;
    }

    @ApiOperation(value = "Method getting all races")
    @GetMapping(value = "/races")
    public List<Race> getAllRaces() {
        return raceService.getRaces();
    }

    @ApiOperation(value = "Method for getting race by id")
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/race/{id}")
    public Race getRace(@PathVariable Long id) {
        return raceService.getRaceById(id);
    }

    @ApiOperation(value = "Method for creating new race")
    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/race")
    public ResponseEntity<Race> createNewRace(@RequestBody NewRaceDTO newRace) {

        return raceService.saveNewRace(newRace);
    }

    @ApiOperation(value = "Method for deleting race by id")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/race/{id}")
    public ResponseEntity<Race> deleteRace(@PathVariable Long id) {

        return raceService.deleteRaceById(id);

    }

    @ApiOperation(value = "Method for editing race")
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/race/{id}")
    public ResponseEntity<Race> editRace(@RequestBody NewRaceDTO newRace, @PathVariable Long id) {

        return raceService.editRaceService(newRace, id);
    }


}
