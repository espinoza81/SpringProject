package dogpark.service;

import dogpark.exeption.NoEnoughResourceException;
import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.model.entity.*;
import dogpark.repository.DogRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Service
public class DogService {

    private static final int FOOD_PRICE = 20;
    private static final int LESSON_PRICE = 20;
    private static final int TREAT_PRICE = 30;
    private static final int HEALTH_DECREASE = 10;
    private static final int FOOD_HEALTH_INCREASE = 20;
    private static final int TREAT_HEALTH_INCREASE = 40;
    private static final int STATS_INCREASE = 10;

    private final DogRepository dogRepository;

    @Autowired
    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Optional<DogWithPriceDTO> getDogInfoById(Long dogId) {

        return dogRepository.findById(dogId).
                map(DogWithPriceDTO::new);
    }

    public DogEntity getDog(Long dogId) {
        return dogRepository.findById(dogId)
                .orElseThrow(() -> new ObjectNotFoundException("Dog with ID " + dogId + "not found"));
    }

    public boolean isOwner(String userName, Long dogId) {

        return dogRepository.
                findById(dogId).
                filter(o -> o.getOwner().getEmail().equals(userName)).
                isPresent();
    }

    public boolean isNotForSale(Long dogId) {

        return dogRepository.
                findById(dogId).
                filter(o -> o.getSale() == null).
                isPresent();
    }

    @Transactional
    public void getGroomingProcedure(Long dogId) {
        DogEntity dog = getDog(dogId);

        checkHaveEnoughMoney(dog, LESSON_PRICE);
        checkHaveEnoughHealth(dog);
        checkStatsNotMax(dog.getGrooming(), dog.getBreedEntity().getGrooming());

        decreaseDogHealth(dog);

        int grooming = min(dog.getGrooming() + STATS_INCREASE, dog.getBreedEntity().getGrooming());
        dog.setGrooming(grooming);

        payForProcedure(dog, LESSON_PRICE);

        dogRepository.saveAndFlush(dog);
    }

    @Transactional
    public void getAgilityLesson(Long dogId) {
        DogEntity dog = getDog(dogId);

        checkHaveEnoughMoney(dog, LESSON_PRICE);
        checkHaveEnoughHealth(dog);
        checkStatsNotMax(dog.getAgility(), dog.getBreedEntity().getAgility());

        decreaseDogHealth(dog);

        int agility = min(dog.getAgility() + STATS_INCREASE, dog.getBreedEntity().getAgility());
        dog.setAgility(agility);

        payForProcedure(dog, LESSON_PRICE);

        dogRepository.saveAndFlush(dog);
    }

    @Transactional
    public void getHuntingLesson(Long dogId) {
        DogEntity dog = getDog(dogId);

        checkHaveEnoughMoney(dog, LESSON_PRICE);
        checkHaveEnoughHealth(dog);
        checkStatsNotMax(dog.getHunting(), dog.getBreedEntity().getHunting());

        decreaseDogHealth(dog);

        int hunting = min(dog.getHunting() + STATS_INCREASE, dog.getBreedEntity().getHunting());
        dog.setHunting(hunting);

        payForProcedure(dog, LESSON_PRICE);

        dogRepository.saveAndFlush(dog);
    }

    private void checkStatsNotMax(int dogStat, int breedStat) {
        if (dogStat == breedStat) {
            throw new NoEnoughResourceException("The dog has reached the max of the stats");
        }
    }

    private void checkHaveEnoughHealth(@NotNull DogEntity dog) {
        if (dog.getHealth() < HEALTH_DECREASE) {
            throw new NoEnoughResourceException("The health of dog is too low");
        }
    }

    public void checkHaveEnoughMoney(@NotNull DogEntity dog, int price) {
        if (dog.getOwner().getMoney() < price) {
            throw new NoEnoughResourceException("You do not have enough money!");
        }
    }

    private void checkHealthNotMax(@NotNull DogEntity dog) {
        if (dog.getHealth() == 100) {
            throw new NoEnoughResourceException("The is on max health!");
        }
    }

    private static void payForProcedure(@NotNull DogEntity dog, int price) {
        dog.getOwner().spendMoney(price);
    }

    private static void decreaseDogHealth(@NotNull DogEntity dog) {
        int health = max(dog.getHealth() - HEALTH_DECREASE, 0);
        dog.setHealth(health);
    }

    public void increaseDogHealth(@NotNull DogEntity dog, int healthIncrease) {

        int health = min(dog.getHealth() + healthIncrease, 100);
        dog.setHealth(health);
    }

    public void decreaseAllDogsHealth() {
        List<DogEntity> allDogs = dogRepository.findAll();
        allDogs.forEach(DogService::decreaseDogHealth);
        dogRepository.saveAllAndFlush(allDogs);
    }

    @Transactional
    public void giveFood(Long dogId) {
        DogEntity dog = getDog(dogId);

        checkHaveEnoughMoney(dog, FOOD_PRICE);
        checkHealthNotMax(dog);

        increaseDogHealth(dog, FOOD_HEALTH_INCREASE);

        payForProcedure(dog, FOOD_PRICE);

        dogRepository.saveAndFlush(dog);
    }

    @Transactional
    public void giveTreat(Long dogId) {
        DogEntity dog = getDog(dogId);

        checkHaveEnoughMoney(dog, TREAT_PRICE);
        checkHealthNotMax(dog);

        increaseDogHealth(dog, TREAT_HEALTH_INCREASE);

        payForProcedure(dog, TREAT_PRICE);

        dogRepository.saveAndFlush(dog);
    }

    public void save(DogEntity dog) {
        dogRepository.save(dog);
    }
}