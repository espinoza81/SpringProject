package dogpark.repository;

import dogpark.model.dtos.DogWithNameCupDTO;
import dogpark.model.entity.DogEntity;
import dogpark.model.enums.SexEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DogRepository extends JpaRepository<DogEntity, Long> {

    List<DogEntity> findAllBySaleIsNullAndOwnerEmailIsLike(String email);
    List<DogEntity> findAllBySaleIsNotNullAndOwnerEmailIsNotLike(String email);
    Optional<DogEntity> findByIdAndSaleIsNull(Long id);
    Optional<DogEntity> findByIdAndSexEquals(Long id, SexEnum sex);
    Optional<DogEntity> findByIdAndSexEqualsAndOwnerEmailIsLike(Long id, SexEnum sex, String email);
    List<DogEntity> findAllByOwnerEmailIsLikeAndSexEquals(String email, SexEnum sex);

    @Query("SELECT d " +
            "FROM DogEntity d " +
            "ORDER BY d.awardCup desc " +
            "LIMIT 3")
    List<DogEntity> getTopDogs();
}

//@Query("SELECT new cardealer.domain.custumer.CustomerTotalSalesDto" +
//            "(c.name, count(s), sum(p.price*(1.0-(s.discount/100.0)))) " +
//            "FROM Customer c " +
//            "JOIN c.sales s " +
//            "JOIN s.car car " +
//            "JOIN car.parts p " +
//            "GROUP BY c " +
//            "ORDER BY count(s) desc, sum(p.price*(1-s.discount/100)) desc")