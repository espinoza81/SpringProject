package dogpark.model.dtos;

import dogpark.model.entity.BreedEntity;
import lombok.Getter;

@Getter
public class BreedDTO {
    private String imgURL;

    private int grooming;

    private int hunting;

    private int agility;

    public BreedDTO(BreedEntity breed) {
        this.imgURL = breed.getImgURL();
        this.grooming = breed.getGrooming();
        this.hunting = breed.getHunting();
        this.agility = breed.getAgility();
    }
}
