package spata.schedule2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spata.schedule2.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmail(String email);

}
