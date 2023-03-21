package dogpark.util.validation;

import dogpark.repository.BreedRepository;
import dogpark.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ShelterExistenceValidator implements ConstraintValidator<ValidateShelterExistence, String> {

    private final UserRepository userRepository;

    @Autowired
    public ShelterExistenceValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(ValidateShelterExistence constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String shelterName, ConstraintValidatorContext constraintValidatorContext) {
        return this.userRepository.findUserEntityByShelterName(shelterName).isEmpty();
    }
}
