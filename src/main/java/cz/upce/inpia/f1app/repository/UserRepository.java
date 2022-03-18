package cz.upce.inpia.f1app.repository;

import cz.upce.inpia.f1app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public User findByName(String name);

    public User findByEmail(String email);

}
