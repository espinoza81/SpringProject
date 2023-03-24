package dogpark.repository;

import dogpark.model.entity.DogEntity;
import dogpark.model.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
    List<ParticipantEntity> findByActiveTrue();

    @Query("SELECT p.dog " +
            "FROM ParticipantEntity p " +
            "WHERE p.active = true")
    List<DogEntity> getDogsForActiveCompetition();

    @Query(" SELECT d " +
            "FROM DogEntity d " +
            "Where d.owner.email = :email and d.id not in" +
            "(SELECT p.dog.id " +
            "FROM ParticipantEntity p " +
            "WHERE p.active=true)")
    List<DogEntity> getOwnerDogsNotEnrolled(String email);
}