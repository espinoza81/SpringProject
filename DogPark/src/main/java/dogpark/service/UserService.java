package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.model.dtos.ShelterDTO;
import dogpark.model.dtos.UserRegistrationDTO;
import dogpark.model.entity.BreedEntity;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BreedService breedService;
    private final DogService dogService;

    private static final int FOOD_PRICE = 20;
    private static final int LESSON_PRICE = 20;
    private static final int TREAT_PRICE = 30;
    private static final int DOG_PRICE = 200;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       BreedService breedService,
                       DogService dogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.breedService = breedService;
        this.dogService = dogService;
    }

    @Transactional
    public void registerUser(@Valid UserRegistrationDTO registrationDTO) {

        UserEntity user = UserEntity.builder()
                .gameUsername(registrationDTO.getUsername())
                .shelterName(registrationDTO.getShelterName())
                .email(registrationDTO.getEmail())
                .money(1000)
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .build();

        UserEntity savedUser = userRepository.save(user);


        BreedEntity breed = breedService.getBreedByName(registrationDTO.getBreed());

        DogEntity dog = DogEntity.builder().
                sex(SexEnum.valueOf(registrationDTO.getSex())).
                name(registrationDTO.getDogName()).
                breedEntity(breed).
                health(100).
                owner(savedUser).
                build();

        dogService.save(dog);

//        userRepository.saveAndFlush(savedUser);
    }

    public void decreaseLoggedUserMoney(String email, String operation) {
        UserEntity user = userRepository.findUserEntityByEmail(email).
                orElseThrow(() -> new ObjectNotFoundException("User with email "+ email + "not found"));

        switch (operation) {
            case "food" -> user.spendMoney(FOOD_PRICE);
            case "treat" -> user.spendMoney(TREAT_PRICE);
            case "lesson" -> user.spendMoney(LESSON_PRICE);
        }

        userRepository.saveAndFlush(user);
    }

    public int getMoney(String email) {
        UserEntity user = userRepository.findUserEntityByEmail(email).
                orElseThrow(() -> new ObjectNotFoundException("User with email "+ email + "not found"));
        return user.getMoney();
    }

    @Transactional
    public ShelterDTO getShelterStats(String email) {
        return userRepository
                .findUserEntityByEmail(email)
                .map(ShelterDTO::new)
                .orElseThrow(() -> new ObjectNotFoundException("User with email " + email + "not found"));
    }

    @Transactional
    public List<DogWithPriceDTO> getMyDogs(String email) {
        return userRepository
                .findUserEntityByEmail(email)
                .orElseThrow(() -> new ObjectNotFoundException("User with email " + email + "not found"))
                .getDogs()
                .stream().map(DogWithPriceDTO::new)
                .toList();
    }
}
