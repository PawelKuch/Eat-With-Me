package pl.dskimina.foodsy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dskimina.foodsy.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);
}
