package dogpark.model.dtos;

import dogpark.model.entity.DogEntity;
import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.UserEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ShelterDTO {
    private final String name;
    private final int dogCount;

    private final int cupCount;

    private final int studSum;
    private final int money;

    private final List<SaleStudDTO> studOffers;
    private final List<SaleStudDTO> saleOffers;

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

        this.saleOffers = user.getDogs().stream()
                .filter(dog -> dog.getSale()!=null)
                .map(dogEntity -> new SaleStudDTO(dogEntity.getSale()))
                .toList();

        this.studOffers = new ArrayList<>();

        user.getDogs().stream()
                .map(DogEntity::getStudOffers)
                .forEach( studList ->
                        this.studOffers.addAll(
                                studList.stream()
                                        .filter(PartnerEntity::isActive)
                                        .map(SaleStudDTO::new)
                                        .toList()));
    }
}
