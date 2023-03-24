package dogpark.model.dtos;

import dogpark.model.entity.DogEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DogWithNameCupDTO {

    private String name;
    private int cups;

    public DogWithNameCupDTO(DogEntity dog) {
        this.cups = dog.getAwardCup();
        this.name = dog.getName();
    }
}
