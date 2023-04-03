package dogpark.service;

import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.entity.BreedEntity;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.SaleEntity;
import dogpark.model.entity.UserEntity;
import dogpark.model.enums.SexEnum;
import dogpark.repository.DogRepository;
import dogpark.repository.SaleRepository;
import dogpark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    private final Long COMMON_ID = 1L;
    private final int PRICE = 100;
    @Mock
    private SaleRepository testSaleRepository;

    @Mock
    private DogRepository testDogRepository;

    @Mock
    private UserRepository testUserRepository;

    @Mock
    DogService testDogService;

    @InjectMocks
    private SaleService testSaleService;

    private DogEntity testDog;

    private BreedEntity testBreed;

    private UserEntity testUser;

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
                owner(testUser).
                name("TestName").
                grooming(10).
                hunting(10).
                agility(10).
                health(100).
                build();
        testDog.setId(COMMON_ID);
    }

    @Test
    void createdSale() {
        AddSaleStudDTO addSaleDTO = AddSaleStudDTO.builder().
                dogId(COMMON_ID).price(PRICE).
                build();

        when(testDogRepository.findByIdAndSaleIsNull(addSaleDTO.getDogId())).thenReturn(Optional.of(testDog));

        testSaleService.createdSale(addSaleDTO);

        Mockito.verify(testDogRepository).saveAndFlush(dogArgumentCaptor.capture());

        DogEntity dogWithSale = dogArgumentCaptor.getValue();

        assertEquals(PRICE, dogWithSale.getSale().getPrice());

    }

    @Test
    void buyDog() {
        SaleEntity sale = SaleEntity.builder().
                price(PRICE).
                build();

        sale.setId(COMMON_ID);

        testDog.setSale(sale);

        UserEntity testNewOwner = UserEntity.builder().
                email("newOwner@examle.com").
                money(1000).
                build();

        when(testDogService.getDog(COMMON_ID)).thenReturn(testDog);

        when(testUserRepository.findUserEntityByEmail("email")).
                thenReturn(Optional.of(testNewOwner));

        testSaleService.buyDog(COMMON_ID, "email");

        Mockito.verify(testUserRepository).saveAndFlush(testNewOwner);
        Mockito.verify(testDogRepository).saveAndFlush(dogArgumentCaptor.capture());

        DogEntity saleDog = dogArgumentCaptor.getValue();

        assertEquals("newOwner@examle.com", saleDog.getOwner().getEmail());
    }
}