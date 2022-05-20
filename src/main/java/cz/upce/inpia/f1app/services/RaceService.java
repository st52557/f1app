package cz.upce.inpia.f1app.services;

import cz.upce.inpia.f1app.dto.NewRaceDTO;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.repository.RaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RaceService {

    private final RaceRepository raceRepository;

    public RaceService(RaceRepository raceRepository) {
        this.raceRepository = raceRepository;
    }


    public List<Race> getRaces() {
        return raceRepository.findAll();
    }

    public Race getRaceById(Long id) {
        return raceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find race with id " + id));
    }

    public ResponseEntity<Race> saveNewRace(NewRaceDTO newRace) {

        Race race = new Race();

        race.setRound(newRace.getRound());
        race.setYear(newRace.getYear());
        race.setCircuit(newRace.getCircuit());

        return ResponseEntity.ok(raceRepository.save(race));
    }

    public ResponseEntity<Race> deleteRaceById(Long id) {
        Race race = raceRepository.getById(id);
        raceRepository.deleteById(id);
        return ResponseEntity.ok(race);
    }

    public ResponseEntity<Race> editRaceService(NewRaceDTO newRace, Long id) {

        Race race = raceRepository.findRaceById(id);
        race.setRound(newRace.getRound());
        race.setYear(newRace.getYear());
        race.setCircuit(newRace.getCircuit());

        return ResponseEntity.ok(raceRepository.save(race));

    }

}
