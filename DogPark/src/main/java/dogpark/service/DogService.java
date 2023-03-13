package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.DogDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.SaleEntity;
import dogpark.repository.DogRepository;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public boolean isForSale(Long dogId) {

        return dogRepository.
                findById(dogId).
                filter(o -> o.getSale()!=null).
                isPresent();
    }


    public void increaseDogHealth(Long dogId, int healthIncrease) {
        DogEntity dog = getDog(dogId);

        int health = min(dog.getHealth() + healthIncrease, 100);
        dog.setHealth(health);

        dogRepository.saveAndFlush(dog);
    }

    public void getGroomingProcedure(Long dogId) {
        DogEntity dog = getDog(dogId);

        decreaseDogHealth(dog);

        int grooming = min(dog.getGrooming() + STATS_INCREASE, dog.getBreedEntity().getGrooming());
        dog.setGrooming(grooming);

        dogRepository.saveAndFlush(dog);
    }

    public void getAgilityLesson(Long dogId) {
        DogEntity dog = getDog(dogId);

        decreaseDogHealth(dog);

        int agility = min(dog.getAgility() + STATS_INCREASE, dog.getBreedEntity().getAgility());
        dog.setAgility(agility);

        dogRepository.saveAndFlush(dog);
    }

    public void getHuntingLesson(Long dogId) {
        DogEntity dog = getDog(dogId);

        decreaseDogHealth(dog);

        int hunting = min(dog.getHunting() + STATS_INCREASE, dog.getBreedEntity().getHunting());
        dog.setHunting(hunting);

        dogRepository.saveAndFlush(dog);
    }

    private static void decreaseDogHealth(@NotNull DogEntity dog) {
        int health = max(dog.getHealth() - HEALTH_DECREASE, 0);
        dog.setHealth(health);
    }

    private DogEntity getDog(Long dogId) {
         return dogRepository.findById(dogId)
                        .orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ dogId + "not found"));
    }

    public List<DogWithNameIdDTO> getDogsNotForSale(String username) {
        return dogRepository.findAllBySaleIsNullAndOwnerEmailIsLike(username)
                .stream().map(DogWithNameIdDTO::new)
                .toList();
    }

    public DogEntity getDogIfForSale(Long dogId) {
        return dogRepository.findByIdAndSaleIsNull(dogId)
                .orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ dogId + "not found or already is for sale"));
    }

    public void created(@Valid AddSaleStudDTO addSaleDTO) {

        DogEntity dog = dogRepository.findByIdAndSaleIsNull(addSaleDTO.getDogId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Dog with ID "+ addSaleDTO.getDogId() + "not found or already is for sale"));

        SaleEntity sale = SaleEntity.builder()
                .price(addSaleDTO.getPrice())
                .build();

        dog.setSale(sale);
        dogRepository.saveAndFlush(dog);
    }

    public List<DogDTO> getDogsForSale(String username) {
        return dogRepository.findAllBySaleIsNotNullAndOwnerEmailIsNotLike(username)
                .stream().map(DogDTO::new)
                .toList();
    }
}