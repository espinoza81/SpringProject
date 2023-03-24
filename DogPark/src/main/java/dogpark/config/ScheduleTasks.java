package dogpark.config;

import dogpark.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

    private final CompetitionService competitionService;

    @Autowired
    public ScheduleTasks(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @Scheduled(cron = "* 0 * * * *")
    //@Scheduled(cron = "* */10 * * * *")
    public void doInBackground() {
        competitionService.start();
    }
}
