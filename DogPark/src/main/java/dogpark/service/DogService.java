package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.DogDTO;
import dogpark.model.entity.DogEntity;
import dogpark.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class DogService {

    private static final int HEALTH_DECREASE = 10;
    private static final int STATS_INCREASE = 10;

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Optional<DogDTO> getDogInfoById(Long dogId) {

        return dogRepository.findById(dogId).
                map(DogDTO::new);
    }

    public boolean isOwner(String userName, Long dogId) {

        return dogRepository.
                findById(dogId).
                filter(o -> o.getOwner().getEmail().equals(userName)).
                isPresent();
    }


    public void increaseDogHealth(Long dogId, int healthIncrease) {
        DogEntity dog = getDog(dogId);

        int health = min(dog.getHealth() + healthIncrease, 100);
        dog.setHealth(health);

        dogRepository.saveAndFlush(dog);
    }

    public void decreaseDogHealth(Long dogId) {
        DogEntity dog = getDog(dogId);

        int health = max(dog.getHealth() - HEALTH_DECREASE, 0);
        dog.setHealth(health);

        dogRepository.saveAndFlush(dog);
    }

    public void getGroomingProcedure(Long dogId) {
        DogEntity dog = getDog(dogId);

        int health = max(dog.getHealth() - HEALTH_DECREASE, 0);
        dog.setHealth(health);

        int grooming = min(dog.getGrooming() + STATS_INCREASE, dog.getBreedEntity().getGrooming());
        dog.setGrooming(grooming);

        dogRepository.saveAndFlush(dog);
    }

    public void getAgilityLesson(Long dogId) {
        DogEntity dog = getDog(dogId);

        int health = max(dog.getHealth() - HEALTH_DECREASE, 0);
        dog.setHealth(health);

        int agility = min(dog.getAgility() + STATS_INCREASE, dog.getBreedEntity().getAgility());
        dog.setAgility(agility);

        dogRepository.saveAndFlush(dog);
    }

    public void getHuntingLesson(Long dogId) {
        DogEntity dog = getDog(dogId);

        int health = max(dog.getHealth() - HEALTH_DECREASE, 0);
        dog.setHealth(health);

        int hunting = min(dog.getHunting() + STATS_INCREASE, dog.getBreedEntity().getHunting());
        dog.setHunting(hunting);

        dogRepository.saveAndFlush(dog);
    }

    private DogEntity getDog(Long dogId) {
         return dogRepository.findById(dogId)
                        .orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ dogId + "not found"));
    }
}
