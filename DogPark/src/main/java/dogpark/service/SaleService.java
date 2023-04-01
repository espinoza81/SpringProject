package dogpark.service;

import dogpark.exeption.NoEnoughResourceException;
import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.AddSaleStudDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.dtos.DogWithPriceDTO;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.SaleEntity;
import dogpark.model.entity.UserEntity;
import dogpark.repository.DogRepository;
import dogpark.repository.SaleRepository;
import dogpark.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final DogRepository dogRepository;
    private final UserRepository userRepository;
    private final DogService dogService;


    @Autowired
    public SaleService(SaleRepository saleRepository,
                       DogRepository dogRepository,
                       UserRepository userRepository,
                       DogService dogService) {
        this.saleRepository = saleRepository;
        this.dogRepository = dogRepository;
        this.userRepository = userRepository;
        this.dogService = dogService;
    }

    public List<DogWithPriceDTO> getDogsForSale(String username) {
        return dogRepository.findAllBySaleIsNotNullAndOwnerEmailIsNotLike(username)
                .stream().map(DogWithPriceDTO::new)
                .toList();
    }

    public List<DogWithNameIdDTO> getDogsNotForSale(String username) {
        return dogRepository.findAllBySaleIsNullAndOwnerEmailIsLike(username)
                .stream().map(DogWithNameIdDTO::new)
                .toList();
    }

    public void createdSale(@Valid @NotNull AddSaleStudDTO addSaleDTO) {

        DogEntity dog = dogRepository.findByIdAndSaleIsNull(addSaleDTO.getDogId())
                .orElseThrow(() ->
                        new ObjectNotFoundException("Dog with ID " + addSaleDTO.getDogId() + "not found or already is for sale"));

        SaleEntity sale = SaleEntity.builder()
                .price(addSaleDTO.getPrice())
                .build();

        dog.setSale(sale);
        dogRepository.saveAndFlush(dog);
    }

    @Transactional
    public void buyDog(Long dogId, String email) {
        DogEntity dog = dogService.getDog(dogId);

        UserEntity user = userRepository.findUserEntityByEmail(email).
                orElseThrow(() -> new ObjectNotFoundException("User with email " + email + "not found"));

        if (user.getMoney() < dog.getSale().getPrice()) {
            throw new NoEnoughResourceException("You do not have enough money!");
        }

        int price = dog.getSale().getPrice();

        UserEntity seller = dog.getOwner();

        dog.setOwner(user);
        saleRepository.delete(dog.getSale());
        dog.setSale(null);
        user.spendMoney(price);
        seller.addMoney(price);

        dogRepository.saveAndFlush(dog);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(seller);
    }

    @Transactional
    public void deleteSale(Long dogId) {
        DogEntity dog = dogService.getDog(dogId);
        saleRepository.delete(dog.getSale());
        dog.setSale(null);
        dogRepository.saveAndFlush(dog);
    }
}