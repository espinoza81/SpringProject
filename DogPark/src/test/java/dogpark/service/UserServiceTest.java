package dogpark.service;

import dogpark.model.dtos.UserRegistrationDTO;
import dogpark.model.entity.UserEntity;
import dogpark.model.entity.UserRoleEntity;
import dogpark.model.enums.UserRoleEnum;
import dogpark.repository.UserRepository;
import dogpark.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private final String SHELTER_NAME = "EspinozaDogs";
    private final String EMAIL = "espinoza81@yahoo.com";
    private final String USERNAME = "espinoza";
    private final String PASSWORD = "1qaz2wsx";
    private final String BREED = "Yorkshire terrier";
    private final String SEX = "F";
    private final String DOG_NAME = "Leta";
    private final String ENCODEDPASSWORD = "encoded_password";

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private BreedService mockBreedService;
    @Mock
    private DogService mockDogService;
    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    private UserService toTest;

    @BeforeEach
    void setUp() {
        toTest = new UserService(mockUserRepository,
                mockPasswordEncoder,
                mockBreedService,
                mockDogService,
                mockUserRoleRepository);
    }

    @Test
    void testUserRegistration_SaveInvoked() {

        // ARRANGE

        UserRegistrationDTO testRegistrationDTO = UserRegistrationDTO.builder()
                .shelterName(SHELTER_NAME)
                .email(EMAIL)
                .username(USERNAME)
                .password(PASSWORD)
                .confirmPassword(PASSWORD)
                .breed(BREED)
                .sex(SEX)
                .dogName(DOG_NAME)
                .build();

        when(mockPasswordEncoder.encode(testRegistrationDTO.getPassword())).
                thenReturn(ENCODEDPASSWORD);

        //ACT

        toTest.registerUser(testRegistrationDTO);

        //ASSERT
        verify(mockUserRepository).save(userEntityArgumentCaptor.capture());

        UserEntity actualSavedUser = userEntityArgumentCaptor.getValue();
        assertEquals(testRegistrationDTO.getEmail(), actualSavedUser.getEmail());
        assertEquals(ENCODEDPASSWORD, actualSavedUser.getPassword());
    }

    @Test
    void setAdmin(){
        UserEntity testUser = UserEntity.builder().
                roles(new ArrayList<>()).
                build();

        UserRoleEntity role = UserRoleEntity.builder().
                role(UserRoleEnum.ADMIN).
                build();
        role.setId(1L);

        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(mockUserRoleRepository.findUserRoleEntityByRole(UserRoleEnum.ADMIN)).thenReturn(Optional.of(role));

        toTest.setAdmin(1L);

        Mockito.verify(mockUserRepository).saveAndFlush(userEntityArgumentCaptor.capture());

        UserEntity actualSavedUser = userEntityArgumentCaptor.getValue();

        assertTrue(actualSavedUser.getRoles().stream().anyMatch(r -> r.equals(role)));
    }

    @Test
    void getAdmin(){
        UserEntity testUser = UserEntity.builder().
                roles(new ArrayList<>()).
                build();

        UserRoleEntity role = UserRoleEntity.builder().
                role(UserRoleEnum.ADMIN).
                build();
        role.setId(1L);

        testUser.addRole(role);

        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(mockUserRoleRepository.findUserRoleEntityByRole(UserRoleEnum.ADMIN)).thenReturn(Optional.of(role));

        toTest.getAdmin(1L);

        Mockito.verify(mockUserRepository).saveAndFlush(userEntityArgumentCaptor.capture());

        UserEntity actualSavedUser = userEntityArgumentCaptor.getValue();

        assertFalse(actualSavedUser.getRoles().stream().anyMatch(r -> r.equals(role)));
    }
}