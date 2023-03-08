package dogpark.model.dtos;

import dogpark.model.entity.DogEntity;
import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.SaleEntity;
import dogpark.model.entity.UserEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class ShelterDTO {
    private String name;
    private int dogCount;

    private int cupCount;

    private int studSum;
    private int money;

    private List<PartnerEntity> studOffers;
    private List<SaleEntity> saleOffers;

    public ShelterDTO(UserEntity user) {
        this.name = user.getShelterName();
        this.dogCount = user.getDogs().size();
        this.cupCount = user.getDogs().stream().mapToInt(DogEntity::getAwardCup).sum();
        this.studSum = user
                .getDogs()
                .stream()
                .mapToInt(dog ->
                        dog.getStudOffers().stream()
                                .filter(stud -> !stud.isActive())
                                .mapToInt(PartnerEntity::getPrice)
                                .sum())
                .sum();
        this.money = user.getMoney();
    }
}
