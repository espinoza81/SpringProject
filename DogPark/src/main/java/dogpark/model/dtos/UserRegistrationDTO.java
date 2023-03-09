package dogpark.model.dtos;

import lombok.Getter;

@Getter
public class UserRegistrationDTO {
    private String shelterName;
    private String email;
    private String username;
    private String password;
    private String confirmPassword;
}
