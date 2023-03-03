package dogpark.service;

import dogpark.model.dtos.AddBreedDTO;
import dogpark.model.entity.BreedEntity;
import dogpark.repository.BreedRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BreedService {

    private final BreedRepository breedRepository;

    @Autowired
    public BreedService(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    public void created(@Valid AddBreedDTO addBreedDTO) {

        BreedEntity breed = BreedEntity.builder()
                .name(addBreedDTO.getName())
                .imgURL(addBreedDTO.getImgURL())
                .description(addBreedDTO.getDescription())
                .grooming(addBreedDTO.getGrooming())
                .hunting(addBreedDTO.getHunting())
                .agility(addBreedDTO.getAgility())
                .build();

        this.breedRepository.save(breed);
    }
}
