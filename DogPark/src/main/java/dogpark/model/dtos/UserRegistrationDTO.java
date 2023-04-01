package dogpark.model.dtos;

import dogpark.util.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ValidatePasswordMatch(password = "password", confirmPassword = "confirmPassword")
public class UserRegistrationDTO {
    @Size(min = 5, max = 50, message = "The name of the shelter must be between 5 end 50 symbols long")
    @NotBlank(message = "Name can`t be empty")
    @ValidateShelterExistence(message = "Shelter with this name already exists!")
    private String shelterName;

    @Email(message = "Please enter a valid e-mail")
    @NotBlank(message = "You must enter an e-mail")
    @ValidateEmailExistence(message = "User with this e-mail already exists!")
    private String email;

    @Size(min = 5, max = 50, message = "Username must be between 5 end 50 symbols long")
    @NotBlank(message = "Name can`t be empty")
    @ValidateUsernameExistence(message = "User with this username already exists!")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be at least 8 characters long")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "You must select breed")
    private String breed;
    private String sex;

    @Size(min = 5, max = 50, message = "The name of the dog must be between 5 end 50 symbols long")
    @NotBlank(message = "The name of the dog can`t be empty")
    private String dogName;
}
