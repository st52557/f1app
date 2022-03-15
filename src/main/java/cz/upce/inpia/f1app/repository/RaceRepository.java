package cz.upce.inpia.f1app.repository;

import cz.upce.inpia.f1app.entity.Driver;
import cz.upce.inpia.f1app.entity.Race;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Race, Long> {
}
