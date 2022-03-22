package cz.upce.inpia.f1app;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.entity.Result;
import cz.upce.inpia.f1app.repository.DriverRepository;
import cz.upce.inpia.f1app.repository.RaceRepository;
import cz.upce.inpia.f1app.repository.ResultRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UnitDriverTest {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Test
    void comparingDrivers(){

        Driver driver = new Driver();
        driver.setName("Lando");
        driver.setCode("LAN");
        driver.setBorn(1997);
        driverRepository.save(driver);

        Driver driver2 = new Driver();
        driver2.setName("Max");
        driver2.setCode("MAX");
        driver2.setBorn(1997);
        driverRepository.save(driver2);

        Race race = new Race();
        race.setYear(2020);
        race.setCircuit("Silverstone");
        raceRepository.save(race);

        Race race2 = new Race();
        race2.setYear(2021);
        race2.setCircuit("Monza");
        raceRepository.save(race2);

        Result result = new Result();
        result.setDriver(driver);
        result.setRace(race);
        result.setFastestLap(54);
        result.setLaps(64);
        result.setPoints(20.0);
        result.setPositionStart(10);
        result.setPositionFinal(2);
        resultRepository.save(result);

        Result result2 = new Result();
        result2.setDriver(driver2);
        result2.setRace(race);
        result2.setFastestLap(54);
        result2.setLaps(64);
        result2.setPoints(24.0);
        result2.setPositionStart(5);
        result2.setPositionFinal(1);
        resultRepository.save(result2);

        Result result3 = new Result();
        result3.setDriver(driver2);
        result3.setRace(race2);
        result3.setFastestLap(54);
        result3.setLaps(64);
        result3.setPoints(24.0);
        result3.setPositionStart(5);
        result3.setPositionFinal(1);
        resultRepository.save(result3);

        Result result4 = new Result();
        result4.setDriver(driver);
        result4.setRace(race2);
        result4.setFastestLap(54);
        result4.setLaps(64);
        result4.setPoints(20.0);
        result4.setPositionStart(10);
        result4.setPositionFinal(2);
        resultRepository.save(result4);

        int overtakes_1 = resultRepository.countAllOvertakesByDriverId(driver.getId());
        int overtakes_2 = resultRepository.countAllOvertakesByDriverId(driver2.getId());

        double points_1 = resultRepository.countAllPointsByDriverId(driver.getId());
        double points_2 = resultRepository.countAllPointsByDriverId(driver2.getId());

        int wins_1 = resultRepository.countAllWinsByDriverId(driver.getId());
        int wins_2 = resultRepository.countAllWinsByDriverId(driver2.getId());

        Assertions.assertThat(overtakes_1).isEqualTo(16);
        Assertions.assertThat(overtakes_2).isEqualTo(8);

        Assertions.assertThat(points_1).isEqualTo(40);
        Assertions.assertThat(points_2).isEqualTo(48);

        Assertions.assertThat(wins_1).isEqualTo(0);
        Assertions.assertThat(wins_2).isEqualTo(2);

        Assertions.assertThat(points_1-points_2).isEqualTo(-8);

    }


}
