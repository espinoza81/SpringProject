package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.ShelterDTO;
import dogpark.model.dtos.UserRegistrationDTO;
import dogpark.model.entity.UserEntity;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private static final int FOOD_PRICE = 20;
    private static final int LESSON_PRICE = 20;
    private static final int TREAT_PRICE = 30;
    private static final int DOG_PRICE = 200;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public void registerUser(UserRegistrationDTO registrationDTO,
                             Consumer<Authentication> successfulLoginProcessor) {

        UserEntity userEntity = UserEntity.builder()
                .gameUsername(registrationDTO.getUsername())
                .shelterName(registrationDTO.getShelterName())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .build();

        userRepository.save(userEntity);

        UserDetails userDetails = userDetailsService.loadUserByUsername(registrationDTO.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        successfulLoginProcessor.accept(authentication);
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
