package dogpark.service;

import dogpark.exeption.NoEnoughResourceException;
import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.SaleEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
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
    private final UserRepository userRepository;;


    public DogService(DogRepository dogRepository,
                      UserRepository userRepository) {
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
    }

    public Optional<DogWithPriceDTO> getDogInfoById(Long dogId) {

        return dogRepository.findById(dogId).
                map(DogWithPriceDTO::new);
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


    public void increaseDogHealth(@NotNull DogEntity dog, int healthIncrease) {

        int health = min(dog.getHealth() + healthIncrease, 100);
        dog.setHealth(health);
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

    private void checkStatsNotMax(int dogStat, int breedStat) {
        if(dogStat == breedStat){
            throw new NoEnoughResourceException("The dog has reached the max of the stats");
        }
    }

    private void checkHaveEnoughHealth(@NotNull DogEntity dog) {
        if(dog.getHealth() < HEALTH_DECREASE) {
            throw new NoEnoughResourceException("The health of dog is too low");
        }
    }


    private static void payForProcedure(@NotNull DogEntity dog, int price) {
        dog.getOwner().spendMoney(price);
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

    public void createdSale(@Valid @NotNull AddSaleStudDTO addSaleDTO) {

        DogEntity dog = dogRepository.findByIdAndSaleIsNull(addSaleDTO.getDogId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Dog with ID "+ addSaleDTO.getDogId() + "not found or already is for sale"));

        SaleEntity sale = SaleEntity.builder()
                .price(addSaleDTO.getPrice())
                .build();

        dog.setSale(sale);
        dogRepository.saveAndFlush(dog);
    }

    public List<DogWithPriceDTO> getDogsForSale(String username) {
        return dogRepository.findAllBySaleIsNotNullAndOwnerEmailIsNotLike(username)
                .stream().map(DogWithPriceDTO::new)
                .toList();
    }


    @Transactional
    public void createdStud(@Valid @NotNull AddSaleStudDTO addStudDTO) {

        DogEntity dog = dogRepository.findByIdAndSexEquals(addStudDTO.getDogId(), SexEnum.M)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Dog with ID "+ addStudDTO.getDogId() + "not found or her sex is F"));

        PartnerEntity studOffer = PartnerEntity.builder()
                .price(addStudDTO.getPrice())
                .dog(dog)
                .active(true)
                .build();

        dog.addStudOffer(studOffer);

        dogRepository.saveAndFlush(dog);


    }

    public List<DogWithNameIdDTO> getDogsSexM(String username) {
        return dogRepository.findAllByOwnerEmailIsLikeAndSexEquals(username, SexEnum.M)
                .stream()
                .map(DogWithNameIdDTO::new)
                .toList();
    }

    public void bueDog(Long dogId, String email) {
        DogEntity dog = getDog(dogId);
        UserEntity user = userRepository.findUserEntityByEmail(email).
                orElseThrow(() -> new ObjectNotFoundException("User with email "+ email + "not found"));

        int price = dog.getSale().getPrice();

        UserEntity seller = dog.getOwner();

        dog.setOwner(user);
        dog.setSale(null);
        //TODO: must delete sale?
        user.spendMoney(price);
        seller.addMoney(price);

        dogRepository.saveAndFlush(dog);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(seller);

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

    private void checkHealthNotMax(@NotNull DogEntity dog) {
        if(dog.getHealth() == 100) {
            throw new NoEnoughResourceException("The is on max health!");
        }
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

    public void checkHaveEnoughMoney(@NotNull DogEntity dog, int price){
        if(dog.getOwner().getMoney() < price){
            throw new NoEnoughResourceException("You do not have enough money!");
        }
    }
}