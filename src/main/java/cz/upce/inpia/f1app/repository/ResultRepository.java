package cz.upce.inpia.f1app.repository;

import cz.upce.inpia.f1app.dto.DriverCumulativeSumPoints;
import cz.upce.inpia.f1app.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long>, JpaSpecificationExecutor<Result> {

    @Query("SELECT re FROM Result re WHERE re.driver.code = ?1")
    public List<Result> findAllResultsByDriverCode(String code);

    public List<Result> findAllByDriverId(Long id);

    @Query("SELECT SUM(re.points) FROM Result re WHERE re.driver.id = ?1")
    public Double countAllPointsByDriverId(Long id);

    @Query(value = "SELECT COUNT(*) FROM Result as re WHERE re.driver_id = ?1 AND re.position_final = 1", nativeQuery = true)
    public int countAllWinsByDriverId(Long id);

    @Query("SELECT SUM(re.positionStart-re.positionFinal) AS DIFF FROM Result re WHERE re.driver.id = ?1")
    public int countAllOvertakesByDriverId(Long id); // More like places gaind...

    @Query(value = "SELECT r.round, r.year, re.points, sum(re.points) OVER (ORDER BY r.year, r.round) AS cumPoints FROM Result re JOIN driver d on (d.id = re.driver_id) JOIN race r on (r.id = re.race_id) WHERE d.id = ?1 ORDER BY (r.year, r.round)", nativeQuery = true)
    public List<Tuple> getCumulativePointsByDriverId(Long id);

}