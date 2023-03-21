package dogpark.repository;

import dogpark.model.entity.BreedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface BreedRepository extends JpaRepository<BreedEntity, Long> {
    Optional<BreedEntity> findByName(String name);
}
