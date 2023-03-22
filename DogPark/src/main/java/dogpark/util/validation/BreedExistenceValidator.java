package dogpark.util.validation;

import dogpark.repository.BreedRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class BreedExistenceValidator implements ConstraintValidator<ValidateBreedExistence, String> {

    private final BreedRepository breedRepository;

    @Autowired
    public BreedExistenceValidator(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    @Override
    public void initialize(ValidateBreedExistence constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return this.breedRepository.findByName(name).isEmpty();
    }
}
