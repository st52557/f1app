package cz.upce.inpia.f1app.repository;

import cz.upce.inpia.f1app.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query(" select re from Result re where re.driver.code = ?1")
    List<Result> findAllResultsByDriverCode(String code);

}
