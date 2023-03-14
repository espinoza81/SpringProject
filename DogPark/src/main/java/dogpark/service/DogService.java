package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.SaleEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import dogpark.repository.PartnerRepository;
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

    private static final int HEALTH_DECREASE = 10;
    private static final int STATS_INCREASE = 10;

    private final DogRepository dogRepository;
    private final PartnerRepository partnerRepository;

    public DogService(DogRepository dogRepository,
                      PartnerRepository partnerRepository) {
        this.dogRepository = dogRepository;
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

    public void createdSale(@Valid AddSaleStudDTO addSaleDTO) {

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
    public void createdStud(@Valid AddSaleStudDTO addStudDTO) {

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
}