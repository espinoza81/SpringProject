package dogpark.model.dtos;

import dogpark.util.validation.ValidateBreedExistence;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBreedDTO {

    @Size(min = 2, max = 50, message = "The name of the breed must be between 2 end 50 symbols long")
    @NotBlank(message = "Name can`t be empty")
    @ValidateBreedExistence(message = "This breed already exists!")
    private String name;


    private String description;

    @Size(min = 2, max = 50, message = "Image URL must be between 2 end 50 symbols long")
    @NotBlank(message = "Image URL can`t be empty")
    private String imgURL;

    @Range(min = 10, max = 100, message = "The points of the grooming stats must between 10 and 100")
    private int grooming;

    @Range(min = 10, max = 100, message = "The points of the hunting stats must between 10 and 100")
    private int hunting;

    @Range(min = 10, max = 100, message = "The points of the agility stats must between 10 and 100")
    private int agility;
}
