package dogpark.service;

import dogpark.exeption.NoEnoughResourceException;
import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.BreedingIdsDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.dtos.SaleStudDTO;
import dogpark.model.entity.BreedEntity;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import dogpark.repository.PartnerRepository;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Math.min;

@Service
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final DogRepository dogRepository;
    private final UserRepository userRepository;

    @Autowired
    public PartnerService(PartnerRepository partnerRepository,
                          DogRepository dogRepository,
                          UserRepository userRepository) {
        this.partnerRepository = partnerRepository;
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
    }

    public List<SaleStudDTO> getDogsForStud() {
        return partnerRepository.findAllByActive(true)
                .stream().map(SaleStudDTO::new)
                .toList();
    }

    public List<DogWithNameIdDTO> getDogsSex(String username, SexEnum sex) {
        return dogRepository.findAllByOwnerEmailIsLikeAndSexEquals(username, sex)
                .stream()
                .map(DogWithNameIdDTO::new)
                .toList();
    }

    @Transactional
    public void createdStud(@Valid @NotNull AddSaleStudDTO addStudDTO) {

        DogEntity dog = dogRepository.findByIdAndSexEquals(addStudDTO.getDogId(), SexEnum.M)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Dog with ID " + addStudDTO.getDogId() + "not found or her sex is F"));

        PartnerEntity studOffer = PartnerEntity.builder()
                .price(addStudDTO.getPrice())
                .dog(dog)
                .active(true)
                .build();

        dog.addStudOffer(studOffer);

        dogRepository.saveAndFlush(dog);
    }

    @Transactional
    public void breedDog(BreedingIdsDTO breedingIdsDTO, String username) {

        long offerId = breedingIdsDTO.getStudId();

        PartnerEntity offer = partnerRepository.findByIdAndActive(offerId, true)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Offer with ID " + offerId + "not found or it`s not active"));

        UserEntity user = userRepository.findUserEntityByEmail(username).
                orElseThrow(() -> new ObjectNotFoundException("User with email " + username + "not found"));

        if (user.getMoney() < offer.getPrice()) {
            throw new NoEnoughResourceException("You do not have enough money!");
        }

        long femaleId = breedingIdsDTO.getFemaleId();

        DogEntity female = dogRepository.findByIdAndSexEqualsAndOwnerEmailIsLike(femaleId, SexEnum.F, username)
                .orElseThrow(() ->
                        new ObjectNotFoundException("Dog with ID " + femaleId + "not found, sex of the dog is male or this is not your dog"));

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

        int grooming = min((female.getGrooming() + male.getGrooming()) / 2, puppyBreed.getGrooming());

        int hunting = min((female.getHunting() + male.getHunting()) / 2, puppyBreed.getHunting());

        int agility = min((female.getAgility() + male.getAgility()) / 2, puppyBreed.getAgility());

        puppy.setGrooming(grooming);
        puppy.setHunting(hunting);
        puppy.setAgility(agility);

        offer.setActive(false);

        user.spendMoney(offer.getPrice());

        offer.getDog().getOwner().addMoney(offer.getPrice());

        partnerRepository.saveAndFlush(offer);

        dogRepository.save(puppy);
    }
}
