package dogpark.model.dtos;

import dogpark.model.entity.DogEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DogWithNameIdDTO {
    private final Long id;
    private final String name;

    public DogWithNameIdDTO(DogEntity dog) {
        this.id = dog.getId();
        this.name = dog.getName();
    }
}
