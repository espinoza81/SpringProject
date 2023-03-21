package dogpark.repository;

import dogpark.model.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
//    List<SaleEntity> getAllByDog_OwnerEmailIsNotLike(String email);
}
