package dogpark.model.dtos;

import dogpark.model.entity.BreedEntity;
import lombok.Getter;

@Getter
public class BreedInfoDTO {
    private String name;
    private String description;
    private String imgURL;
    private int grooming;
    private int hunting;
    private int agility;

    public BreedInfoDTO(BreedEntity breed) {
        this.name = breed.getName();
        this.description = breed.getDescription();
        this.imgURL = breed.getImgURL();
        this.grooming = breed.getGrooming();
        this.hunting = breed.getHunting();
        this.agility = breed.getAgility();
    }
}
