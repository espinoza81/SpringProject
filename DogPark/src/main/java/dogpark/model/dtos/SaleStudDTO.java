package dogpark.model.dtos;

import dogpark.model.entity.PartnerEntity;
import dogpark.model.entity.SaleEntity;
import lombok.Getter;

@Getter
public class SaleStudDTO {
    private Long id;
    private DogDTO dog;
    private int price;

    public SaleStudDTO(SaleEntity sale) {
        this.id = sale.getId();
        this.dog = new DogDTO(sale.getDog());
        this.price = sale.getPrice();
    }

    public SaleStudDTO(PartnerEntity stud) {
        this.id = stud.getId();
        this.dog = new DogDTO(stud.getDog());
        this.price = stud.getPrice();
    }
}
