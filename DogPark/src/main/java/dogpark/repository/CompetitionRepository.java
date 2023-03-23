package dogpark.repository;

import dogpark.model.dtos.DogWithNameCupDTO;
import dogpark.model.entity.CompetitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<CompetitionEntity, Long> {
    Optional<CompetitionEntity> findByActiveTrue();


    @Query("SELECT d.name as name, d.awardCup as cups " +
            "FROM CompetitionEntity c " +
            "JOIN c.participants p " +
            "JOIN p.dog d " +
            "WHERE c.active = true")
    List<DogWithNameCupDTO> getDogsForActiveCompetition();
}
