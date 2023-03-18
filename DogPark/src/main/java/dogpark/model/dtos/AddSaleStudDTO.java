package dogpark.model.dtos;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddSaleStudDTO {
    @Positive
    private Long dogId;
    @Positive(message = "The price must be positive number")
    private int price;
}
