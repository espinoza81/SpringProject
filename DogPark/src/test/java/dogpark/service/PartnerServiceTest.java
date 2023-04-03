package dogpark.service;

import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.BreedingIdsDTO;
import dogpark.model.entity.BreedEntity;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import dogpark.repository.PartnerRepository;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.method.P;

import java.util.Optional;

import static java.lang.Math.min;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartnerServiceTest {

    private final Long COMMON_ID = 1L;
    @Mock
    private PartnerRepository testPartnerRepository;

    @Mock
    private DogRepository testDogRepository;

    @Mock
    private UserRepository testUserRepository;

    @InjectMocks
    private PartnerService testPartnerService;

    private DogEntity testMaleDog;

    private BreedEntity testBreed;

    private UserEntity testMaleOwner;

    @Captor
    private ArgumentCaptor<DogEntity> dogArgumentCaptor;

    @Captor
    private ArgumentCaptor<PartnerEntity> offerArgumentCaptor;

    @BeforeEach
    void setUp() {
        testBreed = BreedEntity.builder().
                name("Chihuahua").
                grooming(90).
                hunting(50).
                agility(70).
                build();
        testBreed.setId(COMMON_ID);

        testMaleOwner = UserEntity.builder().
                money(1000).
                build();

        testMaleDog = DogEntity.builder().
                sex(SexEnum.M).
                breedEntity(testBreed).
                owner(testMaleOwner).
                name("TestName").
                grooming(10).
                hunting(10).
                agility(10).
                health(100).
                build();
        testMaleDog.setId(COMMON_ID);

    }

    @Test
    void breedDog() {
        BreedingIdsDTO breedingIdsDTO = BreedingIdsDTO.builder().
                studId(COMMON_ID).
                femaleId(COMMON_ID).
                name("NewPuppy").
                build();

        PartnerEntity offer = PartnerEntity.builder().
                dog(testMaleDog).
                active(true).
                price(100).
                build();

        UserEntity testFemaleOwner = UserEntity.builder().
                money(1000).
                build();

        DogEntity testFemaleDog = DogEntity.builder().
                sex(SexEnum.F).
                breedEntity(testBreed).
                owner(testFemaleOwner).
                name("TestName").
                grooming(40).
                hunting(40).
                agility(40).
                health(100).
                build();
        testFemaleDog.setId(2L);

        when(testPartnerRepository.findByIdAndActive(breedingIdsDTO.getStudId(), true)).
                thenReturn(Optional.of(offer));

        when(testUserRepository.findUserEntityByEmail("username")).
                thenReturn(Optional.of(testFemaleOwner));

        when(testDogRepository.findByIdAndSexEqualsAndOwnerEmailIsLike(breedingIdsDTO.getFemaleId(), SexEnum.F, "username")).
                thenReturn(Optional.of(testFemaleDog));

        testPartnerService.breedDog(breedingIdsDTO, "username");

        Mockito.verify(testDogRepository).save(dogArgumentCaptor.capture());
        Mockito.verify(testPartnerRepository).saveAndFlush(offerArgumentCaptor.capture());

        DogEntity puppy = dogArgumentCaptor.getValue();

        int expectedAgility = min((testFemaleDog.getAgility() + testMaleDog.getAgility()) / 2, puppy.getBreedEntity().getAgility());

        assertEquals(expectedAgility, puppy.getAgility());

    }
}