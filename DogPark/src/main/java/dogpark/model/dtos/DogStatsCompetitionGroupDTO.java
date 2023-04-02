package dogpark.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DogStatsCompetitionGroupDTO {
    private String name;
    private long money;
    private long award;
}
