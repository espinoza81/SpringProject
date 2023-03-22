package dogpark.service;

import dogpark.exeption.NoEnoughResourceException;
import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.BreedingIdsDTO;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.entity.*;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import dogpark.repository.PartnerRepository;
import dogpark.repository.SaleRepository;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    private final UserRepository userRepository;
    private final SaleRepository saleRepository;

    private final PartnerRepository partnerRepository;


    public DogService(DogRepository dogRepository,
                      UserRepository userRepository,
                      SaleRepository saleRepository,
                      PartnerRepository partnerRepository) {
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
        this.saleRepository = saleRepository;
        this.partnerRepository = partnerRepository;
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

    public boolean isNotForSale(Long dogId) {

        return dogRepository.
                findById(dogId).
                filter(o -> o.getSale()==null).
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

    private static void decreaseDogHealth(@NotNull DogEntity dog) {
        int health = max(dog.getHealth() - HEALTH_DECREASE, 0);
        dog.setHealth(health);
    }

    public void increaseDogHealth(@NotNull DogEntity dog, int healthIncrease) {

        int health = min(dog.getHealth() + healthIncrease, 100);
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

    @Transactional
    public Long breedDog(BreedingIdsDTO breedingIdsDTO, String username) {

        long offerId = breedingIdsDTO.getStudId();

        PartnerEntity offer = partnerRepository.findByIdAndActive(offerId, true)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Offer with ID "+ offerId + "not found or it`s not active"));

        UserEntity user = userRepository.findUserEntityByEmail(username).
                orElseThrow(() -> new ObjectNotFoundException("User with email "+ username + "not found"));

        if(user.getMoney() < offer.getPrice()){
            throw new NoEnoughResourceException("You do not have enough money!");
        }

        long femaleId = breedingIdsDTO.getFemaleId();

        DogEntity female = dogRepository.findByIdAndSexEqualsAndOwnerEmailIsLike(femaleId, SexEnum.F, username)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Dog with ID "+ femaleId + "not found, sex of the dog is male or this is not your dog"));

        DogEntity male = offer.getDog();

        DogEntity puppy = DogEntity.builder().
                name(breedingIdsDTO.getName() + " of " + user.getShelterName()).
                health(100).
                awardCup(0).
                owner(user).
                build();

        Random rand = new Random();
        List<BreedEntity> parentsBreed = Arrays.asList(female.getBreedEntity(), male.getBreedEntity());
        BreedEntity puppyBreed = parentsBreed.get(rand.nextInt(parentsBreed.size()));

        puppy.setBreedEntity(puppyBreed);

        SexEnum sex = SexEnum.randomSex();

        puppy.setSex(sex);

        int grooming = min((female.getGrooming() + male.getGrooming())/2, puppyBreed.getGrooming());

        int hunting = min((female.getHunting() + male.getHunting())/2, puppyBreed.getHunting());

        int agility = min((female.getAgility() + male.getAgility())/2, puppyBreed.getAgility());

        puppy.setGrooming(grooming);
        puppy.setHunting(hunting);
        puppy.setAgility(agility);

        offer.setActive(false);

        user.spendMoney(offer.getPrice());

        offer.getDog().getOwner().addMoney(offer.getPrice());

        partnerRepository.saveAndFlush(offer);

        return dogRepository.save(puppy).getId();
    }

    public List<DogWithNameIdDTO> getDogsSex(String username, SexEnum sex) {
        return dogRepository.findAllByOwnerEmailIsLikeAndSexEquals(username, sex)
                .stream()
                .map(DogWithNameIdDTO::new)
                .toList();
    }

    @Transactional
    public void buyDog(Long dogId, String email) {
        DogEntity dog = getDog(dogId);

        UserEntity user = userRepository.findUserEntityByEmail(email).
                orElseThrow(() -> new ObjectNotFoundException("User with email "+ email + "not found"));

        if(user.getMoney() < dog.getSale().getPrice()){
            throw new NoEnoughResourceException("You do not have enough money!");
        }

        int price = dog.getSale().getPrice();

        UserEntity seller = dog.getOwner();

        dog.setOwner(user);
        saleRepository.delete(dog.getSale());
        dog.setSale(null);
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

    private void checkHealthNotMax(@NotNull DogEntity dog) {
        if(dog.getHealth() == 100) {
            throw new NoEnoughResourceException("The is on max health!");
        }
    }

    public void save(DogEntity dog) {
        dogRepository.save(dog);
    }

    @Transactional
    public void deleteSale(Long dogId) {
        DogEntity dog = getDog(dogId);
        saleRepository.delete(dog.getSale());
        dog.setSale(null);
        dogRepository.saveAndFlush(dog);
    }
}