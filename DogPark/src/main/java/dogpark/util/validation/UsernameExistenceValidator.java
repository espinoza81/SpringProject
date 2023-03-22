package dogpark.util.validation;

import dogpark.repository.BreedRepository;
import dogpark.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernameExistenceValidator implements ConstraintValidator<ValidateUsernameExistence, String> {

    private final UserRepository userRepository;

    @Autowired
    public UsernameExistenceValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(ValidateUsernameExistence constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return this.userRepository.findUserEntityByGameUsername(username).isEmpty();
    }
}
