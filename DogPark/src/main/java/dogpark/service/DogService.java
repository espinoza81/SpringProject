package dogpark.service;

import dogpark.model.dtos.DogDTO;
import dogpark.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DogService {

    private final DogRepository dogRepository;

    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    public Optional<DogDTO> getDogInfoById(Long dogId) {

        return dogRepository.findById(dogId).
                map(DogDTO::new);
    }

    public boolean isOwner(String userName, Long dogId) {

        return dogRepository.
                findById(dogId).
                filter(o -> o.getOwner().getEmail().equals(userName)).
                isPresent();
    }


}
