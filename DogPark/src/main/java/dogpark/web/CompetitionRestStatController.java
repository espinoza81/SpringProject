package dogpark.web;

import dogpark.model.dtos.DogStatsCompetitionGroupDTO;
import dogpark.service.CompetitionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/competitions/stats")
public class CompetitionRestStatController {

    private final CompetitionService competitionService;

    public CompetitionRestStatController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    @GetMapping
    public ResponseEntity<List<DogStatsCompetitionGroupDTO>> getAllDogStats() {
        return ResponseEntity.
                ok(competitionService.getDogStats());
    }
}
