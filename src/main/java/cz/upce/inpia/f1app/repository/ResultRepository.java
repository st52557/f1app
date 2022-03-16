package cz.upce.inpia.f1app.repository;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import cz.upce.inpia.f1app.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query("SELECT re FROM Result re WHERE re.driver.code = ?1")
    public List<Result> findAllResultsByDriverCode(String code);

    public List<Result> findAllByDriverId(Long id);

    @Query(value = "SELECT SUM(Result.points) FROM Result re WHERE re.driver.id = ?1", nativeQuery = true)
    public Double countAllPointsByDriverId(Long id);

}
