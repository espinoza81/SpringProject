package dogpark.repository;

import dogpark.model.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findUserEntityByEmail(String email);
  Optional<UserEntity> findUserEntityByGameUsername(String username);
  Optional<UserEntity> findUserEntityByShelterName(String shelterName);
}
