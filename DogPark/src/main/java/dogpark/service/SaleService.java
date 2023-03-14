package dogpark.service;

import dogpark.model.dtos.SaleStudDTO;
import dogpark.repository.SaleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

//    public List<SaleStudDTO> getDogsForSale(String username) {
//        return saleRepository.getAllByDog_OwnerEmailIsNotLike(username)
//                .stream().map(SaleStudDTO::new)
//                .toList();
//
//    }
}