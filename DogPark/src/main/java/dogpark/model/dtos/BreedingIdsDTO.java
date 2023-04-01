package dogpark.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BreedingIdsDTO {

    @Positive(message = "You must select male dog")
    private long studId;

    @Positive(message = "You must select your female dog")
    private long femaleId;

    @Size(min = 5, max = 50, message = "The name of the dog must be between 5 end 50 symbols long")
    @NotBlank(message = "The name of the dog can`t be empty")
    private String name;
}
