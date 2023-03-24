package dogpark.config;

import dogpark.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

    private final CompetitionService competitionService;
    private final DogService dogService;

    @Autowired
    public ScheduleTasks(CompetitionService competitionService,
                         DogService dogService) {
        this.competitionService = competitionService;
        this.dogService = dogService;
    }
    
    @Scheduled(cron = "0 0 * * * *")
    public void startCompetition() {
        competitionService.start();
    }

    @Scheduled(cron = "0 0 */2 * * *")
    public void decreaseAllDogsHealth() {
        dogService.decreaseAllDogsHealth();
    }
}
