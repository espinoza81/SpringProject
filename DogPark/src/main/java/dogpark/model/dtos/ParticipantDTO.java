package dogpark.model.dtos;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantDTO {

    @Positive
    private Long dogId;
}
