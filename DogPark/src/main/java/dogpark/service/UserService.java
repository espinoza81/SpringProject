package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.ShelterDTO;
import dogpark.model.entity.UserEntity;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final int FOOD_PRICE = 20;
    private static final int LESSON_PRICE = 20;
    private static final int TREAT_PRICE = 30;
    private static final int DOG_PRICE = 200;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
