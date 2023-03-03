package dogpark.model.entity;

import dogpark.model.enums.SexEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "dogs")
public class DogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SexEnum sex;

    @ManyToOne
    private BreedEntity breedEntity;

    private String name;

    private int grooming;

    private int hunting;

    private int agility;

    @OneToOne(fetch = FetchType.EAGER)
    private SaleEntity sale;

    @OneToMany(fetch = FetchType.EAGER)
    private List<PartnerEntity> studOffers;
}
