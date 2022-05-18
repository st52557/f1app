package cz.upce.inpia.f1app.services;

import cz.upce.inpia.f1app.dto.CompareDriversDTO;
import cz.upce.inpia.f1app.dto.inner.Overtakes;
import cz.upce.inpia.f1app.dto.inner.PointsScored;
import cz.upce.inpia.f1app.dto.inner.RacesWon;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    private final ResultRepository resultRepository;

    public DriverService(DriverRepository driverRepository, ResultRepository resultRepository) {
        this.driverRepository = driverRepository;
        this.resultRepository = resultRepository;
    }

    public ResponseEntity<String> getStringResponseEntity(Driver newDriver, Long id) {
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


    public CompareDriversDTO getCompareDriversDTO(Long firstDriverId, Long secondDriverId) {
        CompareDriversDTO compareDriversDTO = new CompareDriversDTO();

        compareDriversDTO.setSecondDriverId(secondDriverId);
        compareDriversDTO.setFirstDriverId(firstDriverId);

        Driver firstDriver = driverRepository.findById(firstDriverId).orElse(null);
        Driver secondDriver = driverRepository.findById(secondDriverId).orElse(null);

        if (firstDriver == null || secondDriver == null) {
            throw new NullPointerException("Driver to compare not found");
        }

        PointsScored pointsScored = new PointsScored();
        pointsScored.setFirstDriver(resultRepository.countAllPointsByDriverId(firstDriver.getId()));
        pointsScored.setSecondDriver(resultRepository.countAllPointsByDriverId(secondDriver.getId()));

        RacesWon racesWon = new RacesWon();
        racesWon.setFirstDriver(resultRepository.countAllWinsByDriverId(firstDriver.getId()));
        racesWon.setSecondDriver(resultRepository.countAllWinsByDriverId(secondDriver.getId()));

        Overtakes overtakes = new Overtakes();
        overtakes.setFirstDriver(resultRepository.countAllOvertakesByDriverId(firstDriver.getId()));
        overtakes.setSecondDriver(resultRepository.countAllOvertakesByDriverId(secondDriver.getId()));

        compareDriversDTO.setPointsScored(pointsScored);
        compareDriversDTO.setRacesWon(racesWon);
        compareDriversDTO.setOvertakes(overtakes);
        return compareDriversDTO;
    }

}
