package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.DogWithNameCupDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.dtos.ParticipantDTO;
import dogpark.model.entity.CompetitionEntity;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.ParticipantEntity;
import dogpark.repository.CompetitionRepository;
import dogpark.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionService {

    private final DogRepository dogRepository;
    private final CompetitionRepository competitionRepository;

    public CompetitionService(DogRepository dogRepository, CompetitionRepository competitionRepository) {
        this.dogRepository = dogRepository;
        this.competitionRepository = competitionRepository;
    }

    public List<DogWithNameIdDTO> getDogsForCompetition(String username) {
        return dogRepository.findAllByOwnerEmailIsLike(username).
                stream().map(DogWithNameIdDTO::new).toList();
    }

    public List<DogWithNameCupDTO> getEnrolledDogs() {
        return competitionRepository.getDogsForActiveCompetition();
    }


    public void enrollDog(ParticipantDTO participantDTO) {
        CompetitionEntity competition = competitionRepository.findByActiveTrue().
                orElseThrow(() -> new ObjectNotFoundException("No active competition found!"));

        DogEntity dog = dogRepository.findById(participantDTO.getDogId()).
                orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ participantDTO.getDogId() + "not found"));

        ParticipantEntity participant = new ParticipantEntity(dog);

        competition.addParticipant(participant);

        competitionRepository.saveAndFlush(competition);
    }
}
