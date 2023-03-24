package dogpark.service;

import dogpark.exeption.ObjectNotFoundException;
import dogpark.model.dtos.DogWithNameCupDTO;
import dogpark.model.dtos.DogWithNameIdDTO;
import dogpark.model.dtos.ParticipantDTO;
import dogpark.model.entity.DogEntity;
import dogpark.model.entity.ParticipantEntity;
import dogpark.repository.DogRepository;
import dogpark.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class CompetitionService {

    private final DogRepository dogRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public CompetitionService(DogRepository dogRepository,
                              ParticipantRepository participantRepository) {
        this.dogRepository = dogRepository;
        this.participantRepository = participantRepository;
    }

    public List<DogWithNameIdDTO> getDogsForCompetition(String username) {
        return participantRepository.getOwnerDogsNotEnrolled(username).
                stream().
                map(DogWithNameIdDTO::new).
                toList();
    }

    public List<DogWithNameCupDTO> getEnrolledDogs() {
        return participantRepository.getDogsForActiveCompetition().
                stream().
                map(DogWithNameCupDTO::new).toList();
    }



    public void enrollDog(ParticipantDTO participantDTO) {

        DogEntity dog = dogRepository.findById(participantDTO.getDogId()).
                orElseThrow(() -> new ObjectNotFoundException("Dog with ID "+ participantDTO.getDogId() + "not found"));

        ParticipantEntity participant = new ParticipantEntity(dog);

        participantRepository.save(participant);
    }

    @Transactional
    public void start(){

        List<ParticipantEntity> participants = participantRepository.findByActiveTrue();

        participants.forEach(p -> p.setScore(this.score(p.getDog())));

        List<ParticipantEntity> sortedParticipant = participants.
                stream().
                sorted(Comparator.comparing(ParticipantEntity::getScore).reversed()).
                toList();

        for (int i = 0; i < sortedParticipant.size(); i++) {
            switch (i) {
                case 0 -> sortedParticipant.get(i).
                        setAward(true).
                        setActive(false).
                        setMoney(200).
                        getDog().
                        addAward().
                        addMoneyToOwner(200);
                case 1 -> sortedParticipant.get(i).
                        setActive(false).
                        setMoney(150).
                        getDog().
                        addMoneyToOwner(150);
                case 2 -> sortedParticipant.get(i).
                        setActive(false).
                        setMoney(100).
                        getDog().
                        addMoneyToOwner(100);
                default -> sortedParticipant.get(i).
                        setActive(false).
                        setMoney(20).
                        getDog().addMoneyToOwner(20);
            }

        }

        participantRepository.saveAllAndFlush(sortedParticipant);
    }

    public int score(DogEntity dog){
        int stats = dog.getAgility() + dog.getGrooming() + dog.getHunting();
        int health = dog.getHealth();
        int random = new Random().nextInt(100);
        int cupIndex = dog.getAwardCup()*10;

        return stats + health + random + cupIndex;
    }
}
