package dogpark.service;

import dogpark.model.entity.UserRoleEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.UserRoleEnum;
import dogpark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    private final String EXISTING_SHELTER_NAME = "EspinozaDog";
    private final String EXISTING_EMAIL = "espinoza81@yahoo.com";
    private final String EXISTING_GAME_USERNAME = "Espinoza";
    private final String EXISTING_PASSWORD = "qazwsx";
    private final String NON_EXISTING_USERNAME = "gosho";
    private ApplicationUserDetailsService mockUserRepo;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp(){
        mockUserRepo = new ApplicationUserDetailsService(mockUserRepository);

    }

    @Test
    void testUserFound(){

        List<UserRoleEntity> roles = new ArrayList<>();

        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setRole(UserRoleEnum.ADMIN);
        roles.add(adminRole);

        UserEntity testUserEntity = UserEntity.builder().
                shelterName(EXISTING_SHELTER_NAME).
                email(EXISTING_EMAIL).
                gameUsername(EXISTING_GAME_USERNAME).
                password(EXISTING_PASSWORD).
                roles(roles).
                build();

        when(mockUserRepository.findUserEntityByEmail(EXISTING_EMAIL)).thenReturn(Optional.of(testUserEntity));
        UserDetails adminDetails = mockUserRepo.loadUserByUsername(EXISTING_EMAIL);

        assertNotNull(adminDetails);
        assertEquals(EXISTING_EMAIL, adminDetails.getUsername());
        assertEquals(EXISTING_PASSWORD, adminDetails.getPassword());

        assertEquals(1, adminDetails.getAuthorities().size());
        assertEquals(roles.get(0).getRole(), UserRoleEnum.ADMIN);
    }

    @Test
    void testUserNotFount(){
        assertThrows(UsernameNotFoundException.class, () -> {
            mockUserRepo.loadUserByUsername(NON_EXISTING_USERNAME);
        });
    }
}
