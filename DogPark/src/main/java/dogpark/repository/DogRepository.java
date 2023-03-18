package dogpark.repository;

import dogpark.model.entity.DogEntity;
import dogpark.model.enums.SexEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DogRepository extends JpaRepository<DogEntity, Long> {

    List<DogEntity> findAllBySaleIsNullAndOwnerEmailIsLike(String email);
    List<DogEntity> findAllBySaleIsNotNullAndOwnerEmailIsNotLike(String email);
    Optional<DogEntity> findByIdAndSaleIsNull(Long id);
    Optional<DogEntity> findByIdAndSexEquals(Long id, SexEnum sex);
    List<DogEntity> findAllByOwnerEmailIsLikeAndSexEquals(String email, SexEnum sex);
}