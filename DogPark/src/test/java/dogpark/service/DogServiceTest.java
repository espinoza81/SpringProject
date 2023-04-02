package dogpark.service;

import dogpark.model.entity.BreedEntity;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import jakarta.persistence.Column;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class DogServiceTest {

    private final Long COMMON_ID = 1L;

    private DogEntity testDog;

    private BreedEntity testBreed;

    private UserEntity testUser;

    @InjectMocks
    private DogService testDogService;

    @Mock
    private DogRepository testDogRepository;

    @Captor
    private ArgumentCaptor<DogEntity> dogArgumentCaptor;

    @BeforeEach
    void setUp() {
        testBreed = BreedEntity.builder().
                name("Chihuahua").
                grooming(90).
                hunting(50).
                agility(70).
                build();
        testBreed.setId(COMMON_ID);

        testUser = UserEntity.builder().
                money(1000).
                build();

        testDog = DogEntity.builder().
                sex(SexEnum.M).
                breedEntity(testBreed).
                name("TestName").
                grooming(10).
                hunting(10).
                agility(10).
                health(100).
                owner(testUser).
                build();
        testDog.setId(COMMON_ID);

        lenient().when(testDogRepository.findById(COMMON_ID)).thenReturn(Optional.of(testDog));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDogInfoById() {
    }

    @Test
    void getDog() {
    }

    @Test
    void isOwner() {
    }

    @Test
    void isNotForSale() {
    }

    @Test
    void getGroomingProcedure() {
        testDogService.getGroomingProcedure(COMMON_ID);

        Mockito.verify(testDogRepository).saveAndFlush(dogArgumentCaptor.capture());

        assertEquals(testDog.getGrooming(), dogArgumentCaptor.getValue().getGrooming());
    }

    @Test
    void getAgilityLesson() {
        testDogService.getAgilityLesson(COMMON_ID);

        Mockito.verify(testDogRepository).saveAndFlush(dogArgumentCaptor.capture());

        assertEquals(testDog.getAgility(), dogArgumentCaptor.getValue().getAgility());
    }

    @Test
    void getHuntingLesson() {
        testDogService.getHuntingLesson(COMMON_ID);

        Mockito.verify(testDogRepository).saveAndFlush(dogArgumentCaptor.capture());

        assertEquals(testDog.getHunting(), dogArgumentCaptor.getValue().getHunting());
    }

    @Test
    void checkHaveEnoughMoney() {
    }

    @Test
    void increaseDogHealth() {
    }

    @Test
    void decreaseAllDogsHealth() {
    }

    @Test
    void giveFood() {
    }

    @Test
    void giveTreat() {
    }

    @Test
    void save() {
    }
}