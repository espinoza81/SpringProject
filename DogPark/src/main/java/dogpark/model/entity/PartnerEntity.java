package dogpark.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "partners")
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private DogEntity dog;

    private int price;

    private boolean isActive;
}
