package dogpark.repository;

import dogpark.model.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    List<PartnerEntity> findAllByActive(boolean active);
}
