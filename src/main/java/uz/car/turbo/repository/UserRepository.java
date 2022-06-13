package uz.car.turbo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.car.turbo.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhoneNumber(String phoneNumber);

}
