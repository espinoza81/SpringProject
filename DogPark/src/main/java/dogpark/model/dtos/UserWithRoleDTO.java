package dogpark.model.dtos;

import dogpark.model.entity.UserEntity;
import dogpark.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserWithRoleDTO {
    private Long id;
    private String gameUsername;
    private boolean isAdmin;

    public UserWithRoleDTO(UserEntity user) {
        this.id = user.getId();
        this.gameUsername = user.getGameUsername();
        this.isAdmin = user.getRoles().
                stream().
                anyMatch(r -> r.getRole().equals(UserRoleEnum.ADMIN));
    }
}
