package dogpark.model.dtos;

import dogpark.model.entity.DogEntity;
import lombok.Getter;

@Getter
public class DogWithPriceDTO {
    private Long id;
    private String sex;
    private String name;
    private BreedDTO breed;

    private int grooming;

    private int hunting;

    private int agility;
    private int health;
    private boolean isForSale;
    private int price;

    public DogWithPriceDTO(DogEntity dog) {
        this.id = dog.getId();
        this.sex = dog.getSex().toString();
        this.breed = new BreedDTO(dog.getBreedEntity());
        this.name = dog.getName();
        this.grooming = dog.getGrooming();
        this.hunting = dog.getHunting();
        this.agility = dog.getAgility();
        this.health = dog.getHealth();
        this.isForSale = dog.getSale() != null;
        if(isForSale) {
            this.price = dog.getSale().getPrice();
        }
    }
}
