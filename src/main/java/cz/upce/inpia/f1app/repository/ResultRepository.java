package cz.upce.inpia.f1app.repository;

import cz.upce.inpia.f1app.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long>, JpaSpecificationExecutor<Result> {

    @Query("SELECT re FROM Result re WHERE re.driver.code = ?1")
    public List<Result> findAllResultsByDriverCode(String code);

    public List<Result> findAllByDriverId(Long id);

    @Query("SELECT SUM(re.points) FROM Result re WHERE re.driver.id = ?1")
    public Double countAllPointsByDriverId(Long id);

    @Query(value = "SELECT COUNT(*) wins FROM Result as re WHERE re.driver_id = 1 AND re.position_final = ?1", nativeQuery = true)
    public int countAllWinsByDriverId(Long id);

    @Query("SELECT SUM(re.positionStart-re.positionFinal) AS DIFF FROM Result re WHERE re.driver.id = ?1")
    public int countAllOvertakesByDriverId(Long id); // More like places gaind...


}
