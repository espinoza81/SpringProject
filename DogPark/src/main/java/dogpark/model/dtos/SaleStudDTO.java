package dogpark.model.dtos;

import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.SaleEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaleStudDTO {
    private Long id;
    private DogWithPriceDTO dog;
    private int price;

    public SaleStudDTO(SaleEntity sale) {
        this.id = sale.getId();
        this.price = sale.getPrice();
    }

    public SaleStudDTO(PartnerEntity stud) {
        this.id = stud.getId();
        this.dog = new DogWithPriceDTO(stud.getDog());
        this.price = stud.getPrice();
    }
}
