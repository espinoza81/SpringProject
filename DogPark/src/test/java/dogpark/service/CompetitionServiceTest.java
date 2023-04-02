package dogpark.service;

import dogpark.model.entity.DogEntity;
import dogpark.model.entity.ParticipantEntity;
import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import dogpark.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTest {
    @Mock
    private DogRepository testDogRepository;

    @Mock
    private ParticipantRepository testParticipantRepository;

    @InjectMocks
    private CompetitionService testCompetitionService;

    @Captor
    private ArgumentCaptor<List<ParticipantEntity>> participantsArgumentCaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getDogsForCompetition() {
    }

    @Test
    void getEnrolledDogs() {
    }

    @Test
    void enrollDog() {
    }

    @Test
    void start() {
        UserEntity testOwner = UserEntity.builder().
                money(1000).
                build();

        DogEntity dogFirst = DogEntity.builder().
                owner(testOwner).
                grooming(10).
                hunting(10).
                agility(10).
                health(10).
                build();
        dogFirst.setId(1L);

        DogEntity dogSecond = DogEntity.builder().
                owner(testOwner).
                grooming(20).
                hunting(20).
                agility(20).
                health(20).
                build();
        dogSecond.setId(2L);

        DogEntity dogThird = DogEntity.builder().
                owner(testOwner).
                grooming(30).
                hunting(30).
                agility(30).
                health(30).
                build();
        dogThird.setId(3L);

        DogEntity dogFour = DogEntity.builder().
                owner(testOwner).
                grooming(40).
                hunting(40).
                agility(40).
                health(40).
                build();
        dogFour.setId(4L);

        ParticipantEntity partFirst = new ParticipantEntity(dogFirst);
        partFirst.setId(1L);
        ParticipantEntity partSec = new ParticipantEntity(dogSecond);
        partSec.setId(2L);
        ParticipantEntity partThird = new ParticipantEntity(dogThird);
        partThird.setId(3L);
        ParticipantEntity partFour = new ParticipantEntity(dogFour);
        partFour.setId(4L);

        List<ParticipantEntity> participants = new ArrayList<>();
        participants.add(partFirst);
        participants.add(partSec);
        participants.add(partThird);
        participants.add(partFour);

        when(testParticipantRepository.findByActiveTrue()).thenReturn(participants);

        testCompetitionService.start();

//        Mockito.verify(testParticipantRepository).saveAndFlush(participantsArgumentCaptor.capture());

    }

    @Test
    void score() {
    }

    @Test
    void getDogStats() {
    }
}