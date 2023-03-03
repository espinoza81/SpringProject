package dogpark.service;

import dogpark.model.dtos.AddBreedDTO;
import dogpark.model.dtos.BreedInfoDTO;
import dogpark.model.entity.BreedEntity;
import dogpark.repository.BreedRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<BreedInfoDTO> getAllBreads() {
        return this.breedRepository.findAll()
                .stream()
                .map(BreedInfoDTO::new)
                .collect(Collectors.toList());
    }
}
