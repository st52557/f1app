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

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UnitRaceResultTest {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Test
    void creatingEntities(){

        Driver driver = new Driver();
        driver.setName("Lando");
        driver.setCode("LAN");
        driver.setBorn(1997);
        driverRepository.save(driver);

        Race race = new Race();
        race.setYear(2021);
        race.setCircuit("Silverstone");
        raceRepository.save(race);

        Result result = new Result();
        result.setDriver(driver);
        result.setRace(race);
        result.setFastestLap(54);
        result.setLaps(64);
        result.setPoints(18.0);
        result.setPositionStart(6);
        result.setPositionFinal(4);
        resultRepository.save(result);

        System.out.println("asdf");
        List<Result> results = resultRepository.findAllResultsByDriverCode("LAN");

        Assertions.assertThat(results.get(0).getRace().getCircuit()).isEqualTo("Silverstone");
        Assertions.assertThat(results.get(0).getDriver().getName()).isEqualTo("Lando");


    }


}
