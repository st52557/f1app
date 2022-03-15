package cz.upce.inpia.f1app.repository;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {

}
