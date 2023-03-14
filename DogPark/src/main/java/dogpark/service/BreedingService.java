package dogpark.service;

import dogpark.model.dtos.SaleStudDTO;
import dogpark.repository.PartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreedingService {
    private final PartnerRepository partnerRepository;

    public BreedingService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public List<SaleStudDTO> getDogsForStud() {
        return partnerRepository.findAllByActive(true)
                .stream().map(SaleStudDTO::new)
                .toList();
    }
}
