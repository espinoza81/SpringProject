package dogpark.service;

import dogpark.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    @Autowired
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
