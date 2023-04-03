package dogpark.service;

import dogpark.model.dtos.ShelterDTO;
import dogpark.model.dtos.UserRegistrationDTO;
import dogpark.model.entity.*;
import dogpark.model.enums.SexEnum;
import dogpark.model.enums.UserRoleEnum;
import dogpark.repository.PartnerRepository;
import dogpark.repository.UserRepository;
import dogpark.repository.UserRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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

    @InjectMocks
    private UserService mockUserService;

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

    @Test
    void shelterStats(){
        UserEntity user = UserEntity.builder().
                money(1000).
                shelterName("Test Shelter Name").
                email("email").
                dogs(new ArrayList<>()).
                build();
        user.setId(1L);

        BreedEntity testBreed = BreedEntity.builder().
                name("Chihuahua").
                grooming(90).
                hunting(50).
                agility(70).
                build();
        testBreed.setId(1L);

        DogEntity dogFirst = DogEntity.builder().
                awardCup(2).
                owner(user).
                breedEntity(testBreed).
                grooming(10).
                hunting(10).
                agility(10).
                health(10).
                sex(SexEnum.M).
                name("First Dog").
                studOffers(new ArrayList<>()).
                build();
        dogFirst.setId(1L);

        DogEntity dogSecond = DogEntity.builder().
                awardCup(1).
                owner(user).
                breedEntity(testBreed).
                grooming(20).
                hunting(20).
                agility(20).
                health(20).
                sex(SexEnum.M).
                name("Second Dog").
                studOffers(new ArrayList<>()).
                build();
        dogSecond.setId(2L);

        DogEntity dogThird = DogEntity.builder().
                breedEntity(testBreed).
                owner(user).
                grooming(30).
                hunting(30).
                agility(30).
                health(30).
                sex(SexEnum.F).
                name("Third Dog").
                studOffers(new ArrayList<>()).
                build();
        dogThird.setId(3L);

        DogEntity dogFour = DogEntity.builder().
                breedEntity(testBreed).
                grooming(40).
                owner(user).
                hunting(40).
                agility(40).
                health(40).
                sex(SexEnum.M).
                name("Four Dog").
                studOffers(new ArrayList<>()).
                build();
        dogFour.setId(4L);

        PartnerEntity offerInactive = PartnerEntity.builder().
                active(false).
                dog(dogFour).
                price(100).
                build();
        offerInactive.setId(1L);

        PartnerEntity offerActive = PartnerEntity.builder().
                active(true).
                dog(dogFour).
                price(100).
                build();
        offerActive.setId(2L);

        SaleEntity sale = SaleEntity.builder().
                price(200).
                build();
        sale.setId(1L);

        dogFour.addStudOffer(offerActive);
        dogFour.addStudOffer(offerInactive);
        dogFour.setSale(sale);



        user.addDog(dogFirst);
        user.addDog(dogSecond);
        user.addDog(dogThird);
        user.addDog(dogFour);

        when(mockUserRepository.findUserEntityByEmail("email")).thenReturn(Optional.of(user));

        ShelterDTO shelterStats = mockUserService.getShelterStats("email");

        assertEquals(3, shelterStats.getCupCount());
        assertEquals(1, shelterStats.getStudOffers().size());
    }
}