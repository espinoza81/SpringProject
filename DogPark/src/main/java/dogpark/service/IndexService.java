package dogpark.service;

import dogpark.model.dtos.DogWithNameCupDTO;
import dogpark.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexService {
    private final DogRepository dogRepository;

    public IndexService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public List<DogWithNameCupDTO> getTopDogs(){
        return dogRepository.getTopDogs();
    }
}
