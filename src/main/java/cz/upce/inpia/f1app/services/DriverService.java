package cz.upce.inpia.f1app.services;

import cz.upce.inpia.f1app.dto.CompareDriversDTO;
import cz.upce.inpia.f1app.dto.NewDriverDTO;
import cz.upce.inpia.f1app.dto.inner.Overtakes;
import cz.upce.inpia.f1app.dto.inner.PointsScored;
import cz.upce.inpia.f1app.dto.inner.RacesWon;
import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final ResultRepository resultRepository;

    public DriverService(DriverRepository driverRepository, ResultRepository resultRepository) {
        this.driverRepository = driverRepository;
        this.resultRepository = resultRepository;
    }

    public List<Driver> getDrivers(String sort) {
        if (!sort.equals("ASC") && !sort.equals("DESC")) {
            return driverRepository.findAll();
        }
        return driverRepository.findAll(Sort.by(Sort.Direction.valueOf(sort), "name"));
    }

    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Could not find a driver with id " + id));
    }

    public ResponseEntity<Driver> saveNewDriver(NewDriverDTO newDriver) {

        Driver driver = new Driver();

        driver.setBorn(newDriver.getBorn());
        driver.setCode(newDriver.getCode());
        driver.setSurename(newDriver.getSurename());
        driver.setName(newDriver.getName());
        driver.setNationalilty(newDriver.getNationalilty());
        driver.setRef_name(newDriver.getRef_name());

        return ResponseEntity.ok(driverRepository.save(driver));
    }

    public ResponseEntity<Driver> deleteDriverById(Long id) {
        Driver driver = driverRepository.findDriverById(id);
        driverRepository.deleteById(id);
        return ResponseEntity.ok(driver);
    }

    public ResponseEntity<Driver> editDriverService(NewDriverDTO newDriver, Long id) {

        Driver driver = driverRepository.findDriverById(id);
        driver.setCode(newDriver.getCode());
        driver.setName(newDriver.getName());
        driver.setSurename(newDriver.getSurename());
        driver.setBorn(newDriver.getBorn());
        driver.setNationalilty(newDriver.getNationalilty());
        driver.setRef_name(newDriver.getRef_name());

        return ResponseEntity.ok(driverRepository.save(driver));

    }


    public CompareDriversDTO getCompareDrivers(Long firstDriverId, Long secondDriverId) {
        CompareDriversDTO compareDriversDTO = new CompareDriversDTO();

        compareDriversDTO.setSecondDriverId(secondDriverId);
        compareDriversDTO.setFirstDriverId(firstDriverId);

        Driver firstDriver = driverRepository.findById(firstDriverId)
                .orElseThrow(() -> new RuntimeException("Could not find driver with id " + firstDriverId));
        Driver secondDriver = driverRepository.findById(secondDriverId)
                .orElseThrow(() -> new RuntimeException("Could not find driver with id " + secondDriverId));

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
