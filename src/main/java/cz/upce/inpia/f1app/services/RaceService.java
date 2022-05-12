package cz.upce.inpia.f1app.services;

import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class RaceService {

    @Autowired
    private RaceRepository raceRepository;

    public ResponseEntity<String> getStringResponseEntity(Race newRace, Long id) {
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
